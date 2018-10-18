package xyz.baoanj.websocket.service;

import xyz.baoanj.websocket.entity.ChatMessage;

public interface ChatFeedService {
    void broadcastStompChat(ChatMessage msg);
}
