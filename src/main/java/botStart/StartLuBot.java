package botStart;

import command.BotCommandList;
import database.GsonHandler;
import database.object.User;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;


public class StartLuBot {

    public static final Logger LOGGER = Logger.getLogger(LuBot.class);

    public static void main(String[] args) throws TelegramApiException {
        BasicConfigurator.configure();
        LOGGER.setLevel(Level.DEBUG);
        GsonHandler.loadUsers(User.FILE_PATH);

        TelegramBotsApi botsApi = new TelegramBotsApi( DefaultBotSession.class );
        BotCommandList.Commands.initCommand();

        try {
            botsApi.registerBot(new LuBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
