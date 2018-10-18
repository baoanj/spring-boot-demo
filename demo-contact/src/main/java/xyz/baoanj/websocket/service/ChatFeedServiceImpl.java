package xyz.baoanj.websocket.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import xyz.baoanj.websocket.entity.ChatMessage;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChatFeedServiceImpl implements ChatFeedService {
    @Resource
    private SimpMessagingTemplate messaging;

    public void broadcastStompChat(ChatMessage chatMessage) {
        List<String> receiveUsers = chatMessage.getReceiveUsers();

        for (String username : receiveUsers) {
            messaging.convertAndSendToUser(username, "/topic/polo",
                    chatMessage);
        }
    }
}
