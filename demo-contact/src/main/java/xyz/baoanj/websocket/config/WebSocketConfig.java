package xyz.baoanj.websocket.config;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import xyz.baoanj.contacts.entity.UserInfo;
import xyz.baoanj.websocket.web.ChatController;

import java.security.Principal;
import java.util.Map;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(marcoHandler(), "/ws/simple")
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
                                                  WebSocketHandler wsHandler,
                                                  Map<String, Object> map) {
                    String username = (String) map.get("username");
                    return () -> username;
                }
            }).setAllowedOrigins("*");
    }

    @Bean
    public MarcoHandler marcoHandler() {
        return new MarcoHandler();
    }

}
