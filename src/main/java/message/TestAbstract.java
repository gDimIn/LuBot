package message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public abstract class TestAbstract {

    public <T> T action(){
        TestCommandAction testAction = new TestCommandAction();
        MessageBuilder mb = new MessageBuilder().setChatId("").setText("");
        return (T) mb.buildSendMessage();
    }
}
