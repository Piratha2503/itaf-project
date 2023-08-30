package com.ii.testautomation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

//@Configuration
//@EnableWebSocket
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    @Bean
//    public ProgressWebSocketHandler progressWebSocketHandler() {
//        return new ProgressWebSocketHandler();
//    }
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//
//        registry.addHandler(progressWebSocketHandler(), "/calculateProgressPercentage")//.withSockJS();
//          .setAllowedOrigins("http://localHost:8092", "http://192.168.1.34:3005");
//    }
//
//
////    public void registerStompEndpoints(StompEndpointRegistry registry) {
////        registry.addEndpoint("/gs-guide-websocket");
////    }
//
//
//}

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/ws-broadcast");
        registry.setApplicationDestinationPrefixes("/app");
    }
    @Bean
    public ProgressWebSocketHandler progressWebSocketHandler() {
        return new ProgressWebSocketHandler();
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/calculateProgressPercentage").setAllowedOrigins("*").withSockJS();
    }
}