package com.example.TelegramBot.service;

import com.example.TelegramBot.model.ActualWeather;
import com.example.TelegramBot.repository.ActualWeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ActualWeatherService {

    private final ActualWeatherRepository actualWeatherRepository;

    @Autowired
    public ActualWeatherService(ActualWeatherRepository actualWeatherRepository) {
        this.actualWeatherRepository = actualWeatherRepository;
    }

    public ActualWeather findTopByOrderByIdDesc(){
        return actualWeatherRepository.findTopByOrderByIdDesc().orElse(null);
    }
}
