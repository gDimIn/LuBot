package keyboard;

import botStart.LuBot;
import botStart.StartLuBot;
import database.GsonHandler;
import database.object.User;
import keyboard.buttons.*;
import org.slf4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import test.Executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyboardStart implements Executor {

    private ArrayList<KeyboardRow> keyboard = new ArrayList<>();
    private ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    private SendMessage sendMessage = new SendMessage();

    public void applyRow(){
        User user = User.getUserById(sendMessage.getChatId());

        if (keyboard.isEmpty() && user != null) {
            if ( user.isAdmin() ) {
                keyboard.add(new RowMarkup().build(Arrays.asList(
                        new BtnAdministration().get(),
                        new BtnInfoUser().get()
                )));
            } else {
                keyboard.add(new RowMarkup().build(Arrays.asList(
                        new BtnInfoUser().get()
                )));
            }
        }
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
    }


    @Override
    public void setChat(String chatId) {
        sendMessage.setChatId(chatId);
    }

    @Override
    public void setText(String text) {
        sendMessage.setText(text);
    }

    @Override
    public void executeCommand(LuBot bot) throws TelegramApiException {
        new User(Long.parseLong(sendMessage.getChatId()), "Name");
        applyRow();
        sendMessage.setReplyMarkup(keyboardMarkup);
        bot.execute(sendMessage);
    }
}
