package xyz.baoanj.contacts.service;

import xyz.baoanj.contacts.entity.Contact;
import xyz.baoanj.contacts.entity.UserInfo;

import java.util.List;

public interface AmqpSenderService {
    public void sendAllContacts(List<Contact> contacts);
    public void noticeNewContactToAll(UserInfo user);
    public void noticeNewContactToUser(UserInfo user, List<String> noticeUsers);
}
