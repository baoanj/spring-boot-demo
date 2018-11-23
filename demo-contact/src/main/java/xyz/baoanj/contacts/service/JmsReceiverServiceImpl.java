package xyz.baoanj.contacts.service;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import xyz.baoanj.contacts.entity.Contact;

import javax.annotation.Resource;

@Component
public class JmsReceiverServiceImpl implements JmsReceiverService {
    @Resource
    private SimpMessagingTemplate messaging;

    @JmsListener(destination = "contact.all.topic", containerFactory = "jmsTopicFactory")
    public void receiveTopicMessage1(Contact contact) {
        System.out.println("topic-1: " + contact.getFullName());
    }

    @JmsListener(destination = "contact.all.topic", containerFactory = "jmsTopicFactory")
    public void receiveTopicMessage2(Contact contact) {
        System.out.println("topic-2: " + contact.getFullName());
    }

    @JmsListener(destination = "contact.all.topic", containerFactory = "jmsTopicFactory")
    public void receiveTopicMessage3(Contact contact) {
        System.out.println("topic-3: " + contact.getFullName());
    }

    @JmsListener(destination = "contact.all.queue", containerFactory = "jmsQueueFactory")
    public void receiveQueueMessage1(Contact contact) {
        System.out.println("queue-1: " + contact.getPhoneNumber());
    }

    @JmsListener(destination = "contact.all.queue", containerFactory = "jmsQueueFactory")
    public void receiveQueueMessage2(Contact contact) {
        System.out.println("queue-2: " + contact.getPhoneNumber());
    }

    @JmsListener(destination = "contact.all.queue", containerFactory = "jmsQueueFactory")
    public void receiveQueueMessage3(Contact contact) {
        System.out.println("queue-3: " + contact.getPhoneNumber());
    }

    @JmsListener(destination = "contact.new.topic", containerFactory = "jmsTopicFactory")
    public void receiveMessageNewContact(String username) {
        messaging.convertAndSend("/topic/newContact", username);
    }
}
