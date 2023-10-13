package com.example.TelegramBot.service;

import com.example.TelegramBot.model.WeatherResponse;
import com.example.TelegramBot.model.YandexRemoteClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YandexWeatherService implements WeatherService{

    private final YandexRemoteClient yandexRemoteClient;

    @Override
    public WeatherResponse.Response getForecast() {
        return yandexRemoteClient.getForecast();
    }
}
