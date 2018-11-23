package xyz.baoanj.contacts.service;

import xyz.baoanj.contacts.entity.Contact;

public interface JmsReceiverService {
    public void receiveTopicMessage1(Contact contact);
    public void receiveTopicMessage2(Contact contact);
    public void receiveTopicMessage3(Contact contact);
    public void receiveQueueMessage1(Contact contact);
    public void receiveQueueMessage2(Contact contact);
    public void receiveQueueMessage3(Contact contact);
}
