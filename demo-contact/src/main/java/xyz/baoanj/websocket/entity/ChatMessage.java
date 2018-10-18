package xyz.baoanj.websocket.entity;

import java.util.List;

public class ChatMessage {
    private String message;
    private long time;
    private String sendUser;
    private List<String> receiveUsers;

    public ChatMessage() {}

    public ChatMessage(String msg) {
        this.message = msg;
    }

    public ChatMessage(String msg, long time) {
        this.message = msg;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public List<String> getReceiveUsers() {
        return receiveUsers;
    }

    public void setReceiveUsers(List<String> receiveUsers) {
        this.receiveUsers = receiveUsers;
    }
}
