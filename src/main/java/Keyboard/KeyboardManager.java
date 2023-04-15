package Keyboard;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;

public class KeyboardManager implements Keyboard {

    private ArrayList<KeyboardRow> keyboard = new ArrayList<>();
    private ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

    public void update(Long chatId, Update update){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("");

    }

    public static void loadKeyboardList() {
        KeyboardManager km = new KeyboardManager();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Получити qr");
        row1.add("Рахунок LuCoin");
        km.keyboard.add(row1);
        km.keyboardMarkup.setKeyboard(km.keyboard);


    }

    @Override
    public SendMessage message(String chatId, String command) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("");
        return sendMessage;
    }

    @Override
    public SendPhoto photo() {
        return null;
    }
}
