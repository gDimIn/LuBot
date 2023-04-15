package Keyboard;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public interface Keyboard {

    SendMessage message(String chatId, String command);

    SendPhoto photo();
}
