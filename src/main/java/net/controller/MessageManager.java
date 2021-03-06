package net.controller;

import net.core.Status;
import net.log.Log;
import net.model.MessageModel;
import net.model.UserModel;
import net.socket.Session;
import net.util.Json;

import java.util.HashMap;

public class MessageManager {

    public static final int LOGIN = 1;
    public static final int SENDMSG = 2;
    public static final int CREATEROOM = 3;

    public static boolean messageProc(String json, Session session) {
        if (json == null || json.equals("")) {
            return false;
        }
        MessageModel message = (MessageModel) Json.decode(new MessageModel(), json);
        if(message == null) {
            return false;
        }
        String uid;
        switch (message.getType()) {
            case LOGIN:
                uid = message.get("uid");
                boolean rs = UserManager.login(uid, message.get("password"), session);
                if (rs) {
                    message.setType(MessageManager.LOGIN);
                    message.setErrno(Status.LOGIN_SUCCESS);
                    HashMap<String, String> content = new HashMap<>();
                    content.put("uid", uid);
                    content.put("name", uid + Math.random() % 100);
                    message.setContent(content);
                    String send = Json.encode(message);
                    Log.logI(uid + "登录成功！");
                    session.sendData(send);
                }
                break;
            case SENDMSG:
                uid = message.get("to");
                UserModel user = UserManager.get(uid);
                if (user != null && user.getSession() != null) {
                    String msg = message.get("message");
                    user.getSession().sendData(msg);
                    Log.logI(message.get("from") + " send message (" + message.get("message")
                            + ") to " + message.get("to"));
                }
                break;
            case CREATEROOM:
                uid = message.get("creator");
                UserModel creator = UserManager.get(uid);
                if (creator != null) {
                    long roomNumber = RoomManager.createRoom(creator);
                    creator.getSession().sendData(roomNumber + "");
                }
                break;
            default:
                break;
        }
        return true;
    }
}
