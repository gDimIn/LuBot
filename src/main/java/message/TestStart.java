package message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public class TestStart {

    public SendMessage action(){
        TestCommandAction testAction = new TestCommandAction();
        MessageBuilder mb = new MessageBuilder();
        return mb.buildSendMessage();
    }
}
