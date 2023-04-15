package Keyboard;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import java.util.HashMap;


public class CommandFactory {

    static public HashMap<String, Keyboard> commandList = new HashMap<>();
    private Keyboard keyboard;

    public SendMessage executor(String chatId, String command){
        keyboard = commandList.get(command);
        if (keyboard == null) {
            return null;
        }
        return keyboard.message(chatId, command);
    }

    public static void loadCommandList(){
        commandList.put("qr", new KeyboardManager());
        commandList.put("coin", new KeyboardManager());
    }
}
