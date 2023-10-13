package com.example.TelegramBot.service;

import com.example.TelegramBot.model.WeatherResponse;

public interface WeatherService {

    WeatherResponse.Response getForecast();

}
