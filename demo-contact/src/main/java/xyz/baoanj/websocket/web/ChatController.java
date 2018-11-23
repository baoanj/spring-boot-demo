package xyz.baoanj.websocket.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import xyz.baoanj.websocket.entity.ChatMessage;
import xyz.baoanj.websocket.service.ChatFeedService;

import javax.annotation.Resource;
import java.security.Principal;

@Controller
public class ChatController {

    @Resource
    private ChatFeedService chatFeedService;

    @MessageMapping("/marco")
    public void stomp(Principal principal, ChatMessage message) {
        message.setSendUser(principal.getName());
        chatFeedService.broadcastStompChat(message);
    }
}
