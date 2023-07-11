package database.object;

import botStart.StartLuBot;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import database.GsonHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class User {

    private static List<User> userList = new ArrayList<>();

    public static final String FILE_PATH = "UserOfBotLu.json";
    private static final int ACCOUNT_NUMBER_FIRST = 100;
    private static boolean LOAD_EXCEPTION = true;
    private static int version;

    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("accountNumber")
    private int accountNumber;
    @JsonProperty("subscription")
    private boolean subscription;
    @JsonProperty("coin")
    private int coin;
    @JsonProperty("admin")
    private boolean isAdmin;

    public User(long id, String name) {
        this.id = id;
        this.name = name;
        setAccountNumber(ACCOUNT_NUMBER_FIRST);

        if (!hasUser(id) && userList != null) {
            userList.add(this);
            GsonHandler.saveTo(FILE_PATH, userList);
        }
    }

    public User(){
    }

    public static void load() {
        ObjectMapper mapper = new ObjectMapper();

        if (userList == null) userList = new ArrayList<>();
        try {
            userList = mapper.readValue(new File(FILE_PATH), new TypeReference<>() {});
            LOAD_EXCEPTION = false;

            System.out.println("Load data success"); //
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void save() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            mapper.writeValue(new File(FILE_PATH), userList);
            System.out.println("User has saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        GsonHandler.saveTo(FILE_PATH, userList);
    }

    private boolean hasUser(long id) {
        for (User tempUser : userList) {
            if (tempUser.getId() == id) {
                System.out.println("Thit user has register");
                return true;
            }
        }
        return false;
    }

    private boolean hasAccountNumber(int number) {
        for (User tempUser : userList) {
            if (tempUser.getAccountNumber() == number) {
                return true;
            }
        }
        return false;
    }

    private boolean isAdmin(long id) {
        return false;
    }

    public static User getUserById(String id) {
        StartLuBot.LOGGER.info("GetUserById: getInId "+id);
        StartLuBot.LOGGER.info("GetUserById: userList size "+userList.size());
        for (User tempUser : userList) {
            StartLuBot.LOGGER.info("GetUserById: userGetId "+tempUser.getId());

            if (String.valueOf(tempUser.getId()).equals(id)) {
                return tempUser;
            }
        }
        return null;
    }

    public static List<User> getUsers() { return userList == null ? new ArrayList<>() : userList; }
    public static void setUsers(List<User> users) { userList = users; }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCoin() {
        return coin;
    }

    public String getPhone() { return phone; }

    public int getAccountNumber() {
        return accountNumber;
    }

    private void setAccountNumber(int number) {
        if (userList.size() == 0) {
            accountNumber = number;
            return;
        }
        System.out.println("ListSize = "+ userList.size());
        accountNumber = number + userList.size();
    }

    public boolean isSubscription() {
        return subscription;
    }

    public void setSubscription(boolean subscription) {
        this.subscription = subscription;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", coin=" + coin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
