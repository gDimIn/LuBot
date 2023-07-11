package database;

import botStart.StartLuBot;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import database.object.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GsonHandler {

    public static List<?> loadFrom(String pathToFile){
        ObjectMapper mapper = new ObjectMapper();

        try {
            System.out.println("Load data success"); //
            return mapper.readValue(new File(pathToFile), new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }

    public static void saveTo(String pathToFile, List<?> list){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            if (!list.isEmpty()) {
                mapper.writeValue(new File(pathToFile), list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveUser(String pathToFile, User user){
        List<User> list;
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            list = (ArrayList<User>) loadFrom(pathToFile);
            list.add(user);
            mapper.writeValue(new File(pathToFile), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadUsers(String pathToFile){
        ObjectMapper mapper = new ObjectMapper();
        try {
            StartLuBot.LOGGER.info("Load data success "); //
            User.setUsers(mapper.readValue(new File(pathToFile), new TypeReference<>() {}));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
