package keyboard.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.Collection;


public class RowMarkup {

    private KeyboardRow row = new KeyboardRow();

    public KeyboardRow build(Collection<KeyboardButton> buttons){
        row.addAll(buttons);
        return this.row;
    }

}
