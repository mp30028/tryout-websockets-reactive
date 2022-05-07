package com.zonesoft.tryout.websockets.configurations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonesoft.tryout.websockets.entities.Profile;
import com.zonesoft.tryout.websockets.events.ProfileDataEvent;
import com.zonesoft.tryout.websockets.events.ProfileDataEventSource;
import com.zonesoft.tryout.websockets.events.publishers.ProfileDataEventPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
//@EnableWebSocket
public class WebSocketConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfiguration.class);
	
    @Bean
    public Executor executor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public HandlerMapping handlerMapping(WebSocketHandler wsh) {
        return new SimpleUrlHandlerMapping() {
            {
                setUrlMap(Collections.singletonMap("/websockets/events", wsh));
                setOrder(10);
            }
        };
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
    
    @Bean
    public WebSocketHandler webSocketHandler(ProfileDataEventPublisher eventPublisher) {

        Flux<ProfileDataEvent> publish = Flux.create(eventPublisher).share();

        return session -> {

            Flux<WebSocketMessage> messageFlux = publish.map(evt -> {
            	LOGGER.debug("[EVENT-HANDLER-INVOKED]");
//                    Profile profile = (Profile) evt.getSource();
//                    Map<String, String> data = new HashMap<>();
//                    data.put("id", profile.getId());
//                    return profile.getEmail();
            	return ((ProfileDataEventSource) evt.getSource()).toString();
            }).map(str -> {
            	LOGGER.debug("[EVENT-HANDLER-SEND-MSG] message = {}",str);
                return session.textMessage(str);
            });

            return session.send(messageFlux);
        };
    }

//    @Bean
//    public WebSocketHandler webSocketHandler(ObjectMapper objectMapper,ProfileCreatedEventPublisher eventPublisher) {
//
//        Flux<ProfileCreatedEvent> publish = Flux.create(eventPublisher).share();
//
//        return session -> {
//
//            Flux<WebSocketMessage> messageFlux = publish.map(evt -> {
//            	LOGGER.debug("[EVENT-HANDLER-INVOKED]");
//                try {
//                    Profile profile = (Profile) evt.getSource();
//                    Map<String, String> data = new HashMap<>();
//                    data.put("id", profile.getId());
//                    return objectMapper.writeValueAsString(data);
//                } catch (JsonProcessingException e) {
//                	LOGGER.error("[EVENT-HANDLER-ERROR] {}", e.getLocalizedMessage());
//                    throw new RuntimeException(e);
//                }
//            }).map(str -> {
//            	LOGGER.debug("[EVENT-HANDLER-SEND-MSG] message = {}",str);
//                return session.textMessage(str);
//            });
//
//            return session.send(messageFlux);
//        };
//    }

}
