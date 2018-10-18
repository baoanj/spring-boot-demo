package xyz.baoanj.websocket.web;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.baoanj.contacts.entity.UserInfo;
import xyz.baoanj.websocket.entity.ChatMessage;
import xyz.baoanj.websocket.service.ChatFeedService;

import javax.annotation.Resource;
import java.security.Principal;

@Controller
public class ChatController {

    public static UserInfo theUser = null;

    @Resource
    private ChatFeedService chatFeedService;

    @RequestMapping(value = "/ws/checkLoginForWs", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject checkLoginForWebSocket() {
        // Shiro Session
        Subject subject = SecurityUtils.getSubject();
        UserInfo user = (UserInfo) subject.getSession().getAttribute("user");

        theUser = user;

        JSONObject res = new JSONObject();
        if (user == null) {
            res.put("code", 0);
            res.put("msg", "未登录");
        } else {
            res.put("code", 1);
            res.put("username", user.getUsername());
        }
        return res;
    }

    @MessageMapping("/marco")
    public void stomp(Principal principal, ChatMessage message) {
        message.setSendUser(principal.getName());
        chatFeedService.broadcastStompChat(message);
    }
}
