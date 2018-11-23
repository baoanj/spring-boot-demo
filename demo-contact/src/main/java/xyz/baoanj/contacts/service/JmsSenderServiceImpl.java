package xyz.baoanj.contacts.service;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import xyz.baoanj.contacts.entity.Contact;
import xyz.baoanj.contacts.entity.UserInfo;

import javax.jms.Destination;
import java.util.List;

@Service
public class JmsSenderServiceImpl implements JmsSenderService {
    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendAllContacts(List<Contact> contacts) {
        Destination destinationQueue = new ActiveMQQueue("contact.all.queue");
        Destination destinationTopic = new ActiveMQTopic("contact.all.topic");

        for (Contact contact : contacts) {
            jmsTemplate.convertAndSend(destinationQueue, contact);
            jmsTemplate.convertAndSend(destinationTopic, contact);
        }
    }

    public void noticeNewContact(UserInfo user) {
        Destination destination = new ActiveMQTopic("contact.new.topic");
        jmsTemplate.convertAndSend(destination, user.getUsername());
    }
}
