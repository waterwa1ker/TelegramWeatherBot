package com.example.TelegramBot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "actual_weather")
public class ActualWeather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "temp")
    private int temp;

    @Column(name = "feels_like")
    private int feelsLike;

    @Column(name = "condition")
    private String condition;

    @Column(name = "wind_speed")
    private int windSpeed;

    @Column(name = "phenom_condition")
    private String phenomCondition;

    public ActualWeather() {
    }

    public ActualWeather(int id, int temp, int feelsLike, String condition, int windSpeed, String phenomCondition) {
        this.id = id;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.condition = condition;
        this.windSpeed = windSpeed;
        this.phenomCondition = phenomCondition;
    }

    @Override
    public String toString() {
        return "ActualWeather{" +
                "id=" + id +
                ", temp=" + temp +
                ", feelsLike=" + feelsLike +
                ", condition='" + condition + '\'' +
                ", windSpeed=" + windSpeed +
                ", phenomCondition='" + phenomCondition + '\'' +
                '}';
    }
}
