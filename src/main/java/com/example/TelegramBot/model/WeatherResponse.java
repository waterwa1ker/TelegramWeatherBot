package com.example.TelegramBot.model;

import java.util.List;

public interface WeatherResponse {

    record Response(FactResponse fact, List<ForecastsResponse> forecasts){}

    record FactResponse(double temp, double feels_like, String condition, double wind_speed, int prec_type, String phenom_condition){}

    record ForecastsResponse(PartResponse parts){}

    record PartResponse(NightResponse night, MorningResponse morning, DayResponse day, EveningResponse evening){}

    record NightResponse(double temp_min, double temp_max, double temp_avg, double feels_like, String condition, double wind_speed, int prec_type, String phenom_condition){}


    record MorningResponse(double temp_min, double temp_max, double temp_avg, double feels_like, String condition, double wind_speed, int prec_type, String phenom_condition){}


    record DayResponse(double temp_min, double temp_max, double temp_avg, double feels_like, String condition, double wind_speed, int prec_type, String phenom_condition){}


    record EveningResponse(double temp_min, double temp_max, double temp_avg, double feels_like, String condition, double wind_speed, int prec_type, String phenom_condition){}

}
