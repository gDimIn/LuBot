package botStart;

import message.TestStart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import command.BotCommandList;
import test.Executor;

public class LuBot extends TelegramLongPollingBot {

    final static Logger logger = LoggerFactory.getLogger(LuBot.class);
    private static LuBot luBot;

    LuBot() throws TelegramApiException {
        luBot = this;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (update.hasCallbackQuery()){
            StartLuBot.LOGGER.info(update.getCallbackQuery().getData());
            SendMessage sm = new SendMessage();
            sm.setChatId(String.valueOf( update.getCallbackQuery().getFrom().getId() ));
            sm.setText(update.getCallbackQuery().getData());
            try {
                execute(sm);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        if (message.hasText()) {
            String text = message.getText();
            String chatId = String.valueOf(message.getChatId());
            try {
                exec(chatId, message);
            } catch (TelegramApiException e){
                e.printStackTrace();
            }

            /*SendMessage smessage = new SendMessage();
            smessage.setChatId(String.valueOf(update.getMessage().getChatId()));
            smessage.setText("Hello, World!");

            if(text.equals("/qr")) {
                try {
                    QrCodeGenerator qr = new QrCodeGenerator(getBotUsername(), getBotToken(), update);
                    SendPhoto photo = qr.getQRImage("t.me");
                    photo.setChatId(chatId);
                    execute(photo);

                } catch (WriterException | IOException | TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if(text.equals("sendLoc")) {
                SendLocation location = new SendLocation();
                location.setLongitude(message.getLocation().getLongitude());
                location.setLatitude(message.getLocation().getLatitude());
                location.setHorizontalAccuracy(message.getLocation().getHorizontalAccuracy());
                location.setChatId(chatId);
                try {
                    execute(location);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (text.equals("/start")) {
                List<KeyboardRow> keyboard = new ArrayList<>();
                SendMessage sendMessage = new SendMessage();
                KeyboardRow row1 = new KeyboardRow();
                KeyboardRow row2 = new KeyboardRow();
                KeyboardButton btn = new KeyboardButton();
                btn.setText("sendLoc");
                btn.setRequestLocation(true);

                row1.add(btn);
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

            }*/

        }

    }

    public static LuBot getLuBot() {
        return luBot;
    }

    @Override
    public String getBotUsername() {
        return "testXDimanXBot";
    }

    @Override
    public String getBotToken() {
        return "2084121730:AAGJLyJu262NI4gEJTYrnqEI8Axb_yT7i74";
    }

    public void exec(String chatId, Message msg) throws TelegramApiException {
        logger.info(msg.getText());
        logger.info(String.valueOf(msg.getChatId()));
        logger.info( BotCommandList.getByCommand(msg.getText()).toString() );
        Executor sb = BotCommandList.getByCommand(msg.getText());
        sb.setChat(chatId);
        sb.setText(msg.getText());
        sb.executeCommand(this);
    }

}