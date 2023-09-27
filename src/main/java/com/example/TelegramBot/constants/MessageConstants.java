package com.example.TelegramBot.constants;

import lombok.Getter;

@Getter
public enum MessageConstants {

    HELP_TEXT("Этот бот показывает погоду в городе Казань.\n" +
            "Доступные команды:\n\n/start - начать работу бота\n/register - зарегистрировать пользователя\n" +
            "/my_info - вывести сведения о пользователе\n" +
            "/delete_info - удалить сведения о пользователе\n/help - получить справочник о командах"),
    DELETE_TEXT("Пользователь был удален."),
    REGISTER_TEXT("Вы правда хотите зарегистрироваться?"),
    YES_BUTTON("Вы нажали \"Да\""),
    NO_BUTTON("Вы нажали \"Нет\""),
    USER_ALREADY_EXISTS("Вы уже зарегистрированы"),
    SUCCESS_REGISTER("Вы успешно были зарегистрированы");

    private String message;

    MessageConstants(String message) {
        this.message = message;
    }

}
