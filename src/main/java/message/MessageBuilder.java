package message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

public class MessageBuilder {
    private SendMessage message;
    private SendPhoto photo;

    public MessageBuilder() {
        message = new SendMessage();
        photo = new SendPhoto();
    }

    public MessageBuilder setChatId(String chatId){
        message.setChatId(chatId);
        photo.setChatId(chatId);
        return this;
    }

    public MessageBuilder setText(String text){
        message.setText(text);
        return this;
    }

    public MessageBuilder setPhoto(InputFile input){
        photo.setPhoto(input);
        return this;
    }

    public SendMessage buildSendMessage(){
        return message;
    }

    public SendPhoto buildSendPhoto(){
        //return qrGenerator.getQRImage("https://t.me/"+botName+"?start="+command);
        return photo;
    }

}
