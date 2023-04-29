package keyboard.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class BtnRegister implements Button{
    private KeyboardButton btn = new KeyboardButton();

    @Override
    public KeyboardButton get() {
        setName("Register");
        setRequestLoc(false);
        setRequestPhone(false);
        return btn;
    }

    @Override
    public KeyboardButton get(String name) {
        setName(name);
        setRequestLoc(false);
        setRequestPhone(false);
        return btn;
    }

    @Override
    public BtnRegister setName(String text) {
        btn.setText(text);
        return this;
    }

    @Override
    public BtnRegister setRequestLoc(boolean value) {
        btn.setRequestLocation(value);
        return this;
    }

    @Override
    public BtnRegister setRequestPhone(boolean value) {
        btn.setRequestContact(false);
        return this;
    }


}
