package xyz.baoanj.contacts.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import xyz.baoanj.contacts.config.message.AmqpConfig;
import xyz.baoanj.contacts.entity.AmqpNoticeUser;
import xyz.baoanj.contacts.entity.Contact;
import xyz.baoanj.contacts.entity.UserInfo;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AmqpReceiverServiceImpl implements AmqpReceiverService {

    @Resource
    private SimpMessagingTemplate messaging;

    @RabbitListener(queues = AmqpConfig.queueName1)
    public void receiveContact1(Contact contact) {
        System.out.println("Received queue1   <" + contact.getFullName() + ">");
    }

    @RabbitListener(queues = AmqpConfig.queueName2)
    public void receiveContact2(Contact contact) {
        System.out.println("Received queue2   <" + contact.getFullName() + ">");
    }

    @RabbitListener(queues = AmqpConfig.queueName3)
    public void receiveContact3(Contact contact) {
        System.out.println("Received queue3-1 <" + contact.getFullName() + ">");
    }

    @RabbitListener(queues = AmqpConfig.queueName3)
    public void receiveContact4(Contact contact) {
        System.out.println("Received queue3-2 <" + contact.getFullName() + ">");
    }

    @RabbitListener(queues = AmqpConfig.queueName4)
    public void receiveNoticeNewContactToAll(UserInfo user) {
        messaging.convertAndSend("/topic/newContact", user.getUsername());
    }

    @RabbitListener(queues = AmqpConfig.queueName5)
    public void receiveNoticeNewContactToUser(AmqpNoticeUser amqpNoticeUser) {
        List<String> receiveUsers = amqpNoticeUser.getNoticeUsers();
        UserInfo user = amqpNoticeUser.getUser();
        for (String username : receiveUsers) {
            messaging.convertAndSendToUser(username, "/topic/newContact",
                    user.getUsername());
        }
    }
}
