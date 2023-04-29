package keyboard.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class BtnHello implements Button{
    private KeyboardButton btn = new KeyboardButton();

    @Override
    public KeyboardButton get(String name) {
        setName(name);
        setRequestLoc(false);
        setRequestPhone(false);
        return btn;
    }

    @Override
    public KeyboardButton get() {
        setName("Hello");
        setRequestLoc(false);
        setRequestPhone(false);
        return btn;
    }

    @Override
    public BtnHello setName(String text) {
        btn.setText(text);
        return this;
    }

    @Override
    public BtnHello setRequestLoc(boolean value) {
        btn.setRequestLocation(value);
        return this;
    }

    @Override
    public BtnHello setRequestPhone(boolean value) {
        btn.setRequestContact(value);
        return this;
    }
}
