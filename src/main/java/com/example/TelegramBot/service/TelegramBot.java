package com.example.TelegramBot.service;

import com.example.TelegramBot.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    @Autowired
    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message;
        long chatId;
        if (update.hasMessage()){
            message = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            switch (message){
                case "/start":
                    startCommandRecieve(chatId, update.getMessage().getChat().getFirstName());
                    break;
                default: sendMessage(chatId, "К сожалению, команда не найдена.");
            }
        }
    }

    private void startCommandRecieve(long chatId, String name) {
        String answer = "Привет, " + name + ", добро пожаловать!";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage(String.valueOf(chatId), textToSend);
        try {
            execute(message);
        } catch (TelegramApiException ignored) {

        }
    }
}
