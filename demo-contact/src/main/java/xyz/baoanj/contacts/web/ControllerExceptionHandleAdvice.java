package xyz.baoanj.contacts.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ControllerExceptionHandleAdvice {

    @ExceptionHandler
    @ResponseBody
    public JSONObject handleIOException(HttpServletResponse response, Exception e) {
        e.printStackTrace();

        JSONObject res = new JSONObject();

        if (e instanceof MaxUploadSizeExceededException) {
            response.setStatus(400);
            res.put("message", "上传文件太大");
        } else {
            response.setStatus(500);
            res.put("message", "服务端错误");
        }

        return res;
    }

}
