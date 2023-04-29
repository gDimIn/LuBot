package test;

import botStart.LuBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface Executor {
    void setChat(String chatId);
    void setText(String text);
    void executeCommand(LuBot bot) throws TelegramApiException;
}
