package jpcompany.smartwire.web.member.controller.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.SockJsServiceRegistration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        SockJsServiceRegistration sockJS = registry.addEndpoint("/ws")
                .setAllowedOrigins(WebSocketProperties.URL)
                .withSockJS()
                .setInterceptors(new HttpHandshakeInterceptor());

        log.info("SockJS={}", sockJS.getClass());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/logs");
        registry.enableSimpleBroker("/topic");   // Enables a simple in-memory broker
    }
}
