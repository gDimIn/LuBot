package test;

import botStart.LuBot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


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
        sm.setText(GlobalsString.CLASSIC_MESSAGE);
    }

    @Override
    public void executeCommand(LuBot bot) throws TelegramApiException {
        sm.setText(msg);
        bot.execute(sm);
        if (canSendAdmin) {
            sm.setChatId(String.valueOf(1099568547));
            bot.execute(sm);
        }

    }
}
