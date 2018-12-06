package xyz.baoanj.contacts.entity;

import java.util.List;

public class AmqpNoticeUser {
    private UserInfo user;
    private List<String> noticeUsers;

    public AmqpNoticeUser() {}

    public AmqpNoticeUser(UserInfo user, List<String> noticeUsers) {
        this.user = user;
        this.noticeUsers = noticeUsers;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setNoticeUsers(List<String> noticeUsers) {
        this.noticeUsers = noticeUsers;
    }

    public List<String> getNoticeUsers() {
        return noticeUsers;
    }
}
