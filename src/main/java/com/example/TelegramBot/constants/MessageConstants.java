package com.example.TelegramBot.constants;

import lombok.Getter;

@Getter
public enum MessageConstants {

    HELP_TEXT("Этот бот показывает погоду в городе Казань.\n" +
            "Доступные команды:\n\n/start - начать работу бота\n" +
            "/get_forecast - получить прогноз погоды\n" +
            "/register - зарегистрировать пользователя\n" +
            "/my_info - вывести сведения о пользователе\n" +
            "/delete_info - удалить сведения о пользователе\n/help - получить справочник о командах"),
    DELETE_TEXT("Пользователь был удален."),
    REGISTER_TEXT("Для использования данный команды необходимо зарегистрироваться. Используйте /register для регистрации!"),
    USER_ALREADY_EXISTS("Вы уже зарегистрированы"),
    SUCCESS_REGISTER("Вы успешно были зарегистрированы");


    private String message;

    MessageConstants(String message) {
        this.message = message;
    }

}
