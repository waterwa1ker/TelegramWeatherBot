package com.example.TelegramBot.controller;

import com.example.TelegramBot.config.BotConfig;
import com.example.TelegramBot.constants.MessageConstants;
import com.example.TelegramBot.model.ActualWeather;
import com.example.TelegramBot.model.User;
import com.example.TelegramBot.service.ActualWeatherService;
import com.example.TelegramBot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final UserService userService;
    private final ActualWeatherService actualWeatherService;

    @Autowired
    public TelegramBot(BotConfig botConfig, UserService userService, ActualWeatherService actualWeatherService) {
        this.botConfig = botConfig;
        this.userService = userService;
        this.actualWeatherService = actualWeatherService;
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
                    startCommandRecieve(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/register":
                    registerUser(update.getMessage());
                    break;
                case "/my_info":
                    printUserInformation(chatId);
                    break;
                case "/delete_info":
                    deleteUserInformation(chatId);
                    break;
                case "/help":
                    sendMessage(chatId, MessageConstants.HELP_TEXT.getMessage());
                    break;
                default: sendMessage(chatId, "К сожалению, команда не найдена.");
            }
        }
    }

    public void registerUser(Message message) {
        if (userService.findById(message.getChatId()) == null){
            User user = new User();
            Chat chat = message.getChat();
            user.setId(message.getChatId());
            user.setFirstName(chat.getFirstName());
            user.setUserName(chat.getUserName());
            user.setLastName(chat.getLastName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            userService.save(user);
            sendMessage(message.getChatId(), MessageConstants.SUCCESS_REGISTER.getMessage());
        }
        else{
            sendMessage(message.getChatId(), MessageConstants.USER_ALREADY_EXISTS.getMessage());
        }
    }

    public void deleteUserInformation(long chatId) {
        User user = userService.findById(chatId);
        if (user == null){
            sendMessage(chatId, "Пользователь не найден!");
        }
        else{
            userService.delete(user);
            sendMessage(chatId, MessageConstants.DELETE_TEXT.getMessage());
            log.info("Пользователь " + user.getUserName() + " удалил информацию");
        }
    }

    public void startCommandRecieve(long chatId, String name) {
        String answer = "Привет, " + name + ", добро пожаловать!";
        sendMessage(chatId, answer);
    }

    public void printUserInformation(long chatId) {
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

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage(String.valueOf(chatId), textToSend);
        setKeyboardMarkup(message);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void setBotCommands() {
        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand("/start", "start bot"));
        botCommands.add(new BotCommand("/register", "register user"));
        botCommands.add(new BotCommand("/my_info", "get your information"));
        botCommands.add(new BotCommand("/delete_info", "delete your information"));
        botCommands.add(new BotCommand("/help", "get info about commands"));
        try {
            this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot command: " + e.getMessage());
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
        row.add("/register");
        row.add("/help");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);
    }

    @Scheduled(cron = "0 0 6 * * ?")
    private void sendWeather(){
        ActualWeather weather = actualWeatherService.findTopByOrderByIdDesc();
        List<User> users = userService.findAll();
        for (User user : users){
            sendMessage(user.getId(), weather.toString());
        }
    }
}
