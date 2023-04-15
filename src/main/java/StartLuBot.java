import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class StartLuBot {

    private static final Logger LOGGER = Logger.getLogger(LuBot.class);

    public static void main(String[] args) throws TelegramApiException {
        BasicConfigurator.configure();
        LOGGER.setLevel(Level.DEBUG);

        TelegramBotsApi botsApi = new TelegramBotsApi( DefaultBotSession.class );
        try {
            botsApi.registerBot(new LuBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
