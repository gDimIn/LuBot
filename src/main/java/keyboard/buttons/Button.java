package keyboard.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public interface Button {

    Button setName(String text);
    Button setRequestLoc(boolean value);
    Button setRequestPhone(boolean value);
    KeyboardButton get();
    KeyboardButton get(String name);

}
