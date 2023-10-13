package com.example.TelegramBot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
@Setter
public class YandexConfig {

    @Value("${yandex.token}")
    private String token;

}
