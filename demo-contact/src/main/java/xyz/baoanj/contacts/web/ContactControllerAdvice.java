package xyz.baoanj.contacts.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ContactControllerAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public JSONObject handleFileUploadException(HttpServletResponse response) {
        response.setStatus(400);
        JSONObject res = new JSONObject();
        res.put("message", "文件太大");
        return res;
    }

}
