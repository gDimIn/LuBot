package command;

import keyboard.KeyboardRegister;
import keyboard.KeyboardStart;
import test.Executor;
import test.MsgSend;

import java.util.HashMap;
import java.util.Map;

public class BotCommandList {

    private static final HashMap<String, Executor> comandList = new HashMap<>();
    //private static final BotCommandList command = new BotCommandList();

    /*public static BotCommandList get(){
        return command;
    }*/

    public static Executor getByCommand(String cmd) {
        return comandList.get(cmd);
    }

    public static void registerCommand(String cmd, Executor executor) {
        comandList.put(cmd, executor);
    }

    public class Commands {
        public static void initCommand(){
            BotCommandList.registerCommand("/test", new MsgSend("Tect"));
            BotCommandList.registerCommand("/start", new KeyboardStart());
            BotCommandList.registerCommand("Reg", new KeyboardRegister());
            BotCommandList.registerCommand("Back", new KeyboardStart());
            BotCommandList.registerCommand("Hello", new MsgSend("Shut up"));
            BotCommandList.registerCommand("accept", new MsgSend("You data is saved"));
        }
    }
}
