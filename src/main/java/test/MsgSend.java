package test;

import botStart.LuBot;

import database.object.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class MsgSend extends PhotoSend implements Executor {
    SendMessage sm = new SendMessage();
    private String msg;
    public boolean canSendAdmin = true;

    public MsgSend(String text){
        msg = text;
    }

    @Override
    public void setChat(String chatId) {
        sm.setChatId(chatId);
    }

    @Override
    public void setText(String text) {
        sm.setText(String.valueOf(GlobalsString.CLASSIC_MESSAGE));
    }

    @Override
    public void executeCommand(LuBot bot) throws TelegramApiException {
        User me = User.getUserById(sm.getChatId());

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Змінити");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(keyboard);


        String[] inofome = {"Імя: "+me.getName(),"Тел: "+ me.getPhone(),"Номер рахунку: "+ me.getAccountNumber(),"Lu coin: "+me.getCoin()};

        for (int i = 0; i < inofome.length; i++) {
            sm.setText(inofome[i]);
            if (i < inofome.length - 1){

                button.setCallbackData("Replace " + inofome[i].substring(0,inofome[i].indexOf(":")) );
                sm.setReplyMarkup(markup);
                sm.enableMarkdown(true);
            } else { sm.setReplyMarkup(null);}


            bot.execute(sm);
        }
        if (canSendAdmin) {
            //sm.setChatId(String.valueOf(1099568547));
            //bot.execute(sm);
        }

    }
}
