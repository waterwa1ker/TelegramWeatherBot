package com.example.TelegramBot.config;

import feign.Logger;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FeignConfig {

    private final YandexConfig yandexConfig;

    @Bean
    public RequestInterceptor interceptor(){
        return requestTemplate -> {
            requestTemplate.header("X-Yandex-API-Key", yandexConfig.getToken());
        };
    }


}
