package net.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Json {
    public static String encode(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static Object decode(Object obj, String json) {
        Gson gson = new Gson();
        try {
            obj = gson.fromJson(json, obj.getClass());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return obj;

    }
}
