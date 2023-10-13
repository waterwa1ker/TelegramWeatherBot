package com.example.TelegramBot.model;

import com.example.TelegramBot.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "yandexRemoteClient",
        url = "https://api.weather.yandex.ru/v2",
        configuration = FeignConfig.class
)

@Service
public interface YandexRemoteClient {

    @GetMapping(value = "/forecast?lat=55.7887&lon=49.1221&limit=1")
    WeatherResponse.Response getForecast();

}
