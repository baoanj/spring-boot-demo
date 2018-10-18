package xyz.baoanj.websocket.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import xyz.baoanj.websocket.entity.ChatMessage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MarcoHandler extends AbstractWebSocketHandler {
    private static Map<String, WebSocketSession> users = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("WebSocketSession[" + session.getId() + "] Established");
        users.put(session.getPrincipal().getName(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        System.out.println("WebSocketSession[" + session.getId() + "] Closed");
        users.remove(session.getPrincipal().getName());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        JSONObject jo = JSON.parseObject(message.getPayload());
        ChatMessage cm = new ChatMessage(jo.getString("message"),
                jo.getLong("time"));
        cm.setSendUser(session.getPrincipal().getName());

        List<String> receiveUsers = new LinkedList<>();
        JSONArray ja = jo.getJSONArray("receiveUsers");
        for (Object username : ja) {
            receiveUsers.add((String) username);
        }
        cm.setReceiveUsers(receiveUsers);

        sendMessage(receiveUsers, JSONObject.toJSONString(cm));
    }

    public void sendMessage(List<String> receiveUsers, String msg)
            throws Exception {
        for (String username : receiveUsers) {
            if (users.get(username) != null)
                users.get(username).sendMessage(new TextMessage(msg));
        }
    }
}
