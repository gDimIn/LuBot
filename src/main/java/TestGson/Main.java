package TestGson;

import database.GsonHandler;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;
import database.object.User;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final Logger LOGGER = Logger.getLogger(User.class);
    public static final int USER_DATA_VERSION = 1;

    public static void main(String[] args) {
        LOGGER.setLevel(Level.DEBUG);
        User.load();

        //User user = new User(123457);
        User user1 = new User(123455, "text");

        User user3 = User.getUserById("123455");
        if (user3 != null)
            if (user3.getId() != 0) System.out.println(  user3.getCoin() );
    }
}
