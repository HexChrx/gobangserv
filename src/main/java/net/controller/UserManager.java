package net.controller;

import net.log.Log;
import net.model.UserModel;
import net.socket.Session;
import net.util.Json;

import java.util.HashMap;

public class UserManager {
    private static HashMap<String, UserModel> users = new HashMap<>();

    public static boolean login(String uid, String pwd, Session session) {
        //TODO: login verify

        UserModel user = new UserModel(uid, "name" + Math.random()%10, session);
        UserManager.users.put(uid, user);
        Log.logD("login");
        return true;
    }

    public static UserModel get(String uid) {
        return users.get(uid);
    }
}
