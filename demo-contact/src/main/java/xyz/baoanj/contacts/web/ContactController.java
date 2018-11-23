package xyz.baoanj.contacts.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.baoanj.contacts.entity.*;
import xyz.baoanj.contacts.service.BookService;
import xyz.baoanj.contacts.service.ContactService;
import xyz.baoanj.contacts.service.JmsSenderService;
import xyz.baoanj.contacts.service.UserInfoService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

@RestController
@RequestMapping("/contact")
@ControllerAdvice
public class ContactController {

    final static String UPLOAD_PATH = ClassLoader.getSystemResource("")
            .getPath().substring(1) + "uploadFiles/";

    @Resource
    private ContactService contactService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private BookService bookService;
    @Resource
    private JmsSenderService jmsSenderService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getContactsList() {
        List<Contact> contacts = contactService.getAllContacts();
        JSONArray data = JSON.parseArray(JSON.toJSONString(contacts));

        // Dubbo Test
        // bookService.addBook("Java核心技术（卷I）基础知识（第10版）", 988);

        // JMS Test
        // jmsSenderService.sendAllContacts(contacts);

        JSONObject res = new JSONObject();
        res.put("code", 1);
        res.put("list", data);
        return res;
    }

    @RequestMapping(
            value = "/createWithFile",
            method = RequestMethod.POST,
            consumes = "multipart/form-data"
    )
    @ResponseBody
    public JSONObject createNewContactWithFile(
            @RequestParam(value = "upload_files", required = false) List<MultipartFile> attachments,
            @RequestParam("full_name") String fullName,
            @RequestParam("phone_number") String phoneNumber,
            @RequestParam(value = "address", required = false) String addressStr
    ) throws IOException {
        // handle upload files
        if (attachments.size() > 0) {
            File uploadFolder = new File(UPLOAD_PATH);
            if (!uploadFolder.exists()) uploadFolder.mkdirs();
            for (MultipartFile file : attachments) {
                file.transferTo(new File(UPLOAD_PATH + file.getOriginalFilename()));
            }
        }

        JSONObject addressObj = JSON.parseObject(addressStr);
        String province = addressObj.getString("province");
        String city = addressObj.getString("city");
        String detail = addressObj.getString("detail");
        contactService.addContact(fullName, phoneNumber, province, city, detail);

        // Shiro Session
        Subject subject = SecurityUtils.getSubject();
        UserInfo user = (UserInfo) subject.getSession().getAttribute("user");

        jmsSenderService.noticeNewContact(user);

        // response
        JSONObject res = new JSONObject();
        res.put("code", 1);
        res.put("msg", "提交成功:" + user.getUsername());
        return res;
    }

    @RequestMapping(
            value = "/login",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    @ResponseBody
    public JSONObject login(@RequestBody JSONObject obj, HttpServletResponse response)
            throws IOException {
        String username = obj.getString("username");
        String password = obj.getString("password");

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();

        JSONObject res = new JSONObject();

        try {
            subject.login(token);
        } catch (IncorrectCredentialsException e) {
            // response.sendError(401, "用户名或密码错误");
            // or 更可控的一种方式
            response.setStatus(401);
            res.put("message", "用户名或密码错误");
            return res;
        } catch (UnknownAccountException e) {
            response.sendError(401, "该用户不存在");
            return res;
        } catch (AuthenticationException e) {
            response.sendError(401, "登录失败");
            e.printStackTrace();
            return res;
        }

        UserInfo user = (UserInfo) subject.getPrincipal();
        subject.getSession().setAttribute("user", user);

        res.put("code", 1);
        res.put("msg", "登录成功");
        return res;
    }

    @RequestMapping(
            value = "/regist",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    @ResponseBody
    public JSONObject regist(@RequestBody JSONObject obj) {
        String username = obj.getString("username");
        String password = obj.getString("password");
        String role = obj.getString("role");

        userInfoService.insertUser(username, password, role);

        JSONObject res = new JSONObject();
        res.put("code", 1);
        res.put("msg", "注册成功");
        return res;
    }

    @RequestMapping(
            value = "/listFiles",
            method = RequestMethod.GET
    )
    @ResponseBody
    public JSONObject getAllUploadFiles() {
        File uploadFolder = new File(UPLOAD_PATH);
        File[] files = {};
        if (uploadFolder.exists()) files = uploadFolder.listFiles();

        List<Map<String, String>> contents = new LinkedList<>();
        for (File file : files) {
            Map<String, String> content = new HashMap<>();
            content.put("name", file.getName());
            content.put("size", file.length() + "");
            contents.add(content);
        }

        JSONObject res = new JSONObject();
        res.put("code", 1);
        res.put("files", contents);
        return res;
    }

    @RequestMapping(
            value = "/download/{name}",
            method = RequestMethod.GET
    )
    public void download(@PathVariable("name") String name, HttpServletResponse response)
            throws IOException {
        File file = new File(UPLOAD_PATH + name);

        if (file.exists()) {
            // 解决文件名中文乱码
            String fileName = new String(name.getBytes("UTF-8"), "ISO-8859-1");
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);

            byte[] buffer = new byte[(int) file.length()];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            OutputStream os;

            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bis.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            response.sendError(404, "文件不存在");
        }
    }

    @RequestMapping(
            value = "/preview/{name}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public JSONObject preview(@PathVariable("name") String name) throws IOException {
        File file = new File(UPLOAD_PATH + name);
        JSONObject res = new JSONObject();

        if (file.exists()) {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            byte[] encoded = Base64.getEncoder().encode(fileBytes);
            String base64 = new String(encoded, "UTF-8");
            res.put("code", 1);
            res.put("file", base64);
            return res;
        } else {
            res.put("code", 0);
            res.put("msg", "文件不存在");
            return res;
        }
    }
}