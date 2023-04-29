package keyboard;

import botStart.LuBot;
import keyboard.buttons.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import test.Executor;

import java.util.ArrayList;
import java.util.Arrays;

public class KeyboardRegister implements Executor {

    private ArrayList<KeyboardRow> keyboard = new ArrayList<>();
    private static ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    private SendMessage sendMessage = new SendMessage();

    public void applyRow(){
        if (keyboard.isEmpty()) {
            keyboard.add(new RowMarkup().build(Arrays.asList(
                    new BtnSetReg().get(),
                    new BtnBackMain().get("Back")
            )));
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
        sendMessage.setText("Menu registration");
    }

    @Override
    public void executeCommand(LuBot bot) throws TelegramApiException {
        applyRow();
        sendMessage.setReplyMarkup(keyboardMarkup);
        bot.execute(sendMessage);
    }
}
