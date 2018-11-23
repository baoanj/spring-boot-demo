package xyz.baoanj.contacts.service;

import xyz.baoanj.contacts.entity.Contact;
import xyz.baoanj.contacts.entity.UserInfo;

import java.util.List;

public interface JmsSenderService {
    public void sendAllContacts(List<Contact> contacts);
    public void noticeNewContact(UserInfo user);
}
