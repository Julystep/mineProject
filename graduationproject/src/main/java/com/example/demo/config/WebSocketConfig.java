package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        /**
         * 配置代理域，如果url符合此代理域的话就可以想客户端上传消息
         */
        config.enableSimpleBroker("/queue", "/topic");
        /**
         * 设置用户消息的前缀
         */
        config.setUserDestinationPrefix("user");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        /**
         * addEndpoint()将/ws/endpointChat作为路径点
         * setAllowedOrigins()表示消息可以跨域
         * withSockJS()表示支持socketJS的访问
         */
        registry.addEndpoint("/ws/endpointChat").setAllowedOrigins("*").withSockJS();
    }
}
