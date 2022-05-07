package com.zonesoft.tryout.websockets.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.zonesoft.tryout.websockets.handlers.ProfileHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProfileRouter {

    @Bean
    public RouterFunction<ServerResponse> routes(ProfileHandler handler) {
        return route(GET("/profiles"), handler::all)
            .andRoute(GET("/profiles/{id}"), handler::getById)
            .andRoute(DELETE("/profiles/{id}"), handler::deleteById)
            .andRoute(POST("/profiles"), handler::create)
            .andRoute(PUT("/profiles/{id}"), handler::updateById);
    }
}