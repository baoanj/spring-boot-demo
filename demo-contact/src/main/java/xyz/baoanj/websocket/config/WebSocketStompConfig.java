package xyz.baoanj.websocket.config;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/stomp")
            .addInterceptors(new HandshakeInterceptor() {
                @Override
                public boolean beforeHandshake(ServerHttpRequest request,
                                               ServerHttpResponse response,
                                               WebSocketHandler handler,
                                               Map<String, Object> map) {
                    String principal = JSONObject.toJSONString(request.getPrincipal());
                    JSONObject userJO = JSONObject.parseObject(principal);
                    map.put("username", userJO.getJSONObject("object")
                            .getString("username"));
                    return true;
                }

                @Override
                public void afterHandshake(ServerHttpRequest request,
                                           ServerHttpResponse response,
                                           WebSocketHandler handler,
                                           Exception e) {

                }
            }).setHandshakeHandler(new DefaultHandshakeHandler() {
                @Override
                protected Principal determineUser(ServerHttpRequest request,
                                                  WebSocketHandler handler,
                                                  Map<String, Object> map) {
                    String username = (String) map.get("username");
                    return () -> username;
                }
            });

        registry.addEndpoint("/ws/jms")
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request,
                                                   ServerHttpResponse response,
                                                   WebSocketHandler handler,
                                                   Map<String, Object> map) {
                        String principal = JSONObject.toJSONString(request.getPrincipal());
                        JSONObject userJO = JSONObject.parseObject(principal);
                        map.put("username", userJO.getJSONObject("object")
                                                  .getString("username"));
                        return true;
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request,
                                               ServerHttpResponse response,
                                               WebSocketHandler handler,
                                               Exception e) {

                    }
                }).setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request,
                                                      WebSocketHandler handler,
                                                      Map<String, Object> map) {
                        String username = (String) map.get("username");
                        return () -> username;
                    }
                });
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//         registry.enableSimpleBroker("/queue", "/topic"); // 内存代理
        registry.enableStompBrokerRelay("/queue", "/topic"); // STOMP代理 RabbitMQ/ActiveMQ
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

}
