package xyz.baoanj.contacts.service;

import xyz.baoanj.contacts.entity.AmqpNoticeUser;
import xyz.baoanj.contacts.entity.Contact;
import xyz.baoanj.contacts.entity.UserInfo;

public interface AmqpReceiverService {
    public void receiveContact1(Contact contact);
    public void receiveContact2(Contact contact);
    public void receiveContact3(Contact contact);
    public void receiveContact4(Contact contact);
    public void receiveNoticeNewContactToAll(UserInfo user);
    public void receiveNoticeNewContactToUser(AmqpNoticeUser amqpNoticeUser);
}
