import command.CommandFactory;
import message.MessageBuilder;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import photo.QrCodeGenerator;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import test.Executor;
import test.MessageBuild;
import test.SMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LuBot extends TelegramLongPollingBot implements MessageBuild{

    final static Logger logger = LoggerFactory.getLogger(LuBot.class);
    static LuBot luBot = null;
    LuBot(){
        luBot = this;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        CommandFactory cf = new CommandFactory();

        if (message.hasText()) {
            String text = message.getText();
            String chatId = String.valueOf(message.getChatId());

            SendMessage smessage = new SendMessage();
            smessage.setChatId(String.valueOf(update.getMessage().getChatId()));
            smessage.setText("Hello, World!");

            if(text.equals("/qr")) {
                try {
                    QrCodeGenerator qr = new QrCodeGenerator(getBotUsername(), getBotToken(), update);
                    SendPhoto photo = qr.getQRImage("t.me");
                    photo.setChatId(chatId);
                    execute(photo);

                    //execute();
                } catch (WriterException | IOException | TelegramApiException e){
                    e.printStackTrace();
                }
            } else if (text.equals("/start")) {
                List<KeyboardRow> keyboard = new ArrayList<>();
                SendMessage sendMessage = new SendMessage();
                KeyboardRow row1 = new KeyboardRow();
                KeyboardRow row2 = new KeyboardRow();

                row1.add("BTN1_1");
                row1.add("BTN1_2");
                row2.add("BTN2_1");
                row2.add("BTN2_2");
                keyboard.add(row1);
                keyboard.add(row2);

                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                keyboardMarkup.setKeyboard(keyboard);

                sendMessage.setChatId(chatId);
                sendMessage.setText("Select you choice");
                sendMessage.setReplyMarkup(keyboardMarkup);

                try {
                    execute(sendMessage);
                } catch (TelegramApiException e){
                    e.printStackTrace();
                }

            }

        }
    }

    @Override
    public String getBotUsername() {
        return "testXDimanXBot";
    }

    @Override
    public String getBotToken() {
        return "2084121730:AAGJLyJu262NI4gEJTYrnqEI8Axb_yT7i74";
    }


}
