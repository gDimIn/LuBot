import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LuBot extends TelegramLongPollingBot {

    final static Logger logger = LoggerFactory.getLogger(LuBot.class);

    @Override
    public String getBotUsername() {
        return "testXDimanXBot";
    }

    @Override
    public String getBotToken() {
        return "2084121730:AAGJLyJu262NI4gEJTYrnqEI8Axb_yT7i74";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message.hasText()) {
            String text = message.getText();
            String chatId = String.valueOf(message.getChatId());

            SendMessage smessage = new SendMessage();
            smessage.setChatId(String.valueOf(update.getMessage().getChatId()));
            smessage.setText("Hello, World!");

            if(text.equals("/qr")) {
                try {
                    //byte[] qrCode = generateQRCode(String.valueOf(message.getFrom().getId()));
                    //InputStream is = new ByteArrayInputStream(qrCode);
                    QrCodeGenerator qr = new QrCodeGenerator(getBotUsername(), getBotToken(), update);
                    SendPhoto photo = qr.getQRImage("t.me");

                    //sendPhoto(chatId, is);
                    execute(photo);
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

    /*private byte[] generateQRCode(String id) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 250;
        int height = 250;
        BitMatrix bitMatrix = qrCodeWriter.encode(id, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);
        return bos.toByteArray();
    }

    private void sendPhoto(String chatId, InputStream photo) throws TelegramApiException {
        InputFile inputFile = new InputFile(photo,"qr_code.png");

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(inputFile);
        logger.info(chatId);
        execute(sendPhoto);
    }*/

}
