package io.github.StardewValley.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.HashMap;
import java.util.Map;

public class SavaToJson {
    private static final String FILE = "users.json";
    public static Map<String, User> users = null;

    public static void ensureLoaded() {
        if (users == null) load();
    }

    public static void load() {
        try {
            FileHandle file = Gdx.files.local(FILE);
            if (file.exists()) {
                Json json = new Json();
                users = json.fromJson(HashMap.class, User.class, file);
                if (users == null) users = new HashMap<>();
            } else {
                users = new HashMap<>();
                file.writeString("{}", false);
            }
        } catch (Exception e) {
            users = new HashMap<>();
            System.err.println("Error loading users.json: " + e.getMessage());
        }
    }

    public static void save() {
        ensureLoaded();
        Json json = new Json();
        FileHandle file = Gdx.files.local(FILE);
        file.writeString(json.prettyPrint(users), false);
    }

    public static boolean validateLogin(String username, String password) {
        ensureLoaded();
        User user = users.get(username);
        if (user == null) return false;

        String hashedInput = HashUtil.sha256(password);
        return user.password.equals(hashedInput);
    }

    public static boolean userExists(String username) {
        ensureLoaded();
        return users.containsKey(username);
    }

    public static void registerUser(User user) {
        ensureLoaded();
        users.put(user.getUsername(), user);
        save();
    }

    public static User getUser(String username) {
        ensureLoaded();
        return users.get(username);
    }

    public static void addUser(String username, User user) {
        ensureLoaded();
        users.put(username, user);
        save();
    }

    public static Map<String, User> getAllUsers() {
        ensureLoaded();
        return users;
    }

    public static void deleteUser(String username) {
        ensureLoaded();
        users.remove(username);
        save();
    }


}
