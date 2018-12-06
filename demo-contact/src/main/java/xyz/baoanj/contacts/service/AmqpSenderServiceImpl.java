package xyz.baoanj.contacts.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.baoanj.contacts.config.message.AmqpConfig;
import xyz.baoanj.contacts.entity.AmqpNoticeUser;
import xyz.baoanj.contacts.entity.Contact;
import xyz.baoanj.contacts.entity.UserInfo;

import java.util.List;

@Service
public class AmqpSenderServiceImpl implements AmqpSenderService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendAllContacts(List<Contact> contacts) {
        System.out.println("Sending message...");
        for (Contact contact : contacts) {
            rabbitTemplate.convertAndSend(AmqpConfig.topicExchangeName,
                    "contact.amqp.msg.1", contact);
            rabbitTemplate.convertAndSend(AmqpConfig.fanoutExchangeName,
                    "anything", contact);
        }
    }

    public void noticeNewContactToAll(UserInfo user) {
        rabbitTemplate.convertAndSend(AmqpConfig.directExchangeName,
                "contact.amqp.new.all", user);
    }

    public void noticeNewContactToUser(UserInfo user, List<String> noticeUsers) {
        rabbitTemplate.convertAndSend(AmqpConfig.directExchangeName,
                "contact.amqp.new.user", new AmqpNoticeUser(user, noticeUsers));
    }
}
