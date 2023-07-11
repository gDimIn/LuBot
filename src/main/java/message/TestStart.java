package message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class TestStart extends TestAbstract {

    @Override
    public SendMessage action(){
        TestCommandAction testAction = new TestCommandAction();
        MessageBuilder mb = new MessageBuilder().setChatId("").setText("");
        return mb.buildSendMessage();
    }

}
