package com.example.TelegramBot.controller;

import com.example.TelegramBot.config.BotConfig;
import com.example.TelegramBot.model.User;
import com.example.TelegramBot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private static final String HELP_TEXT = "Этот бот показывает погоду в городе Казань.\n" +
            "Доступные команды:\n\n/start - начать работу бота\n/get_weather_forecast - вывести прогноз погоды\n" +
            "/my_info - вывести сведения о пользователе\n" +
            "/delete_info - удалить сведения о пользователе\n/help - получить справочник о командах\n" +
            "/settings - получить настройки";
    private static final String DELETE_TEXT = "Пользователь был удален.";
    private final BotConfig botConfig;
    private final UserService userService;

    @Autowired
    public TelegramBot(BotConfig botConfig, UserService userService) {
        this.botConfig = botConfig;
        this.userService = userService;
        setBotCommands();
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
                    registerUser(update.getMessage());
                    startCommandRecieve(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/my_info":
                    printUserInformation(chatId);
                    break;
                case "/delete_info":
                    deleteUserInformation(chatId);
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                default: sendMessage(chatId, "К сожалению, команда не найдена.");
            }
        }
    }

    private void setBotCommands(){
        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand("/start", "start bot"));
        botCommands.add(new BotCommand("/get_weather_forecast", "get weather forecast"));
        botCommands.add(new BotCommand("/my_info", "get your information"));
        botCommands.add(new BotCommand("/delete_info", "delete your information"));
        botCommands.add(new BotCommand("/help", "get info about commands"));
        botCommands.add(new BotCommand("/settings", "set your preferences"));
        try {
            this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot command: " + e.getMessage());
        }
    }

    private void deleteUserInformation(long chatId) {
        User user = userService.findById(chatId);
        if (user == null){
            sendMessage(chatId, "Пользователь не найден!");
        }
        else{
            userService.delete(user);
            sendMessage(chatId, DELETE_TEXT);
            log.info("Пользователь " + user.getUserName() + " удалил информацию");
        }
    }

    private void printUserInformation(long chatId) {
        User user = userService.findById(chatId);
        if (user == null){
            sendMessage(chatId, "Пользователь не найден!");
        }
        else{
            String answer = "Имя - " + user.getFirstName() +
                    "\nФамилия - " + user.getLastName() +
                    "\nНикнейм - " + user.getUserName() +
                    "\nДата регистрации - " + user.getRegisteredAt();
            sendMessage(chatId, answer);
            log.info("Пользователь " + user.getUserName() + " запросил информацию");
        }
    }

    private void registerUser(Message message) {
        if (userService.findById(message.getChatId()) == null){
            Chat chat = message.getChat();
            User user = new User(message.getChatId(), chat.getFirstName(), chat.getLastName(), chat.getUserName(), new Timestamp(System.currentTimeMillis()));
            userService.save(user);
            log.info("User " + user.getUserName() + " saved!");
        }
    }

    private void startCommandRecieve(long chatId, String name) {
        String answer = "Привет, " + name + ", добро пожаловать!";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage(String.valueOf(chatId), textToSend);
        setKeyboardMarkup(message);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void setKeyboardMarkup(SendMessage message){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/start");
        row.add("/my_info");
        row.add("/delete_info");
        row.add("/help");
        row.add("/settings");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);
    }
}
