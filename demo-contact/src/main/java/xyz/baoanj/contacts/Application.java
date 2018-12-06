package xyz.baoanj.contacts;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableCaching
@EnableJms
@EnableRabbit
@ComponentScan({"xyz.baoanj.contacts", "xyz.baoanj.websocket"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}