package xyz.baoanj.contacts.config.message;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {
    public static final String topicExchangeName = "contact-amqp-exchange-topic";
    public static final String fanoutExchangeName = "contact-amqp-exchange-fanout";
    public static final String directExchangeName = "contact-amqp-exchange-direct";
    public static final String queueName1 = "contact-amqp-queue-1";
    public static final String queueName2 = "contact-amqp-queue-2";
    public static final String queueName3 = "contact-amqp-queue-3";
    public static final String queueName4 = "contact-amqp-queue-4";
    public static final String queueName5 = "contact-amqp-queue-5";

    @Bean
    Queue queue1() {
        return new Queue(queueName1, false);
    }

    @Bean
    Queue queue2() {
        return new Queue(queueName2, false);
    }

    @Bean
    Queue queue3() {
        return new Queue(queueName3, false);
    }

    @Bean
    Queue queue4() {
        return new Queue(queueName4, false);
    }

    @Bean
    Queue queue5() {
        return new Queue(queueName5, false);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(directExchangeName);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(fanoutExchangeName);
    }

    @Bean
    Binding binding1(Queue queue1, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue1).to(topicExchange)
                .with("contact.amqp.msg.#");
    }

    @Bean
    Binding binding2(Queue queue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue2).to(fanoutExchange);
    }

    @Bean
    Binding binding3(Queue queue3, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue3).to(fanoutExchange);
    }

    @Bean
    Binding binding4(Queue queue4, DirectExchange directExchange) {
        return BindingBuilder.bind(queue4).to(directExchange)
                .with("contact.amqp.new.all");
    }

    @Bean
    Binding binding5(Queue queue5, DirectExchange directExchange) {
        return BindingBuilder.bind(queue5).to(directExchange)
                .with("contact.amqp.new.user");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
