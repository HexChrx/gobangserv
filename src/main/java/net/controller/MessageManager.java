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
                boolean rs = UserManager.login(message.get("uid"), message.get("password"), session);
                if (rs) {
                    message.setType(MessageManager.LOGIN);
                    message.setErrno(Status.LOGIN_SUCCESS);
                    message.setContent(new HashMap<>());
                    String send = Json.encode(message);
                    Log.logD(send);
                    session.sendData(send);
                }
                break;
            case SENDMSG:
                uid = message.get("to");
                UserModel user = UserManager.get(uid);
                if (user != null && user.getSession() != null) {
                    String msg = message.get("message");
                    user.getSession().sendData(msg);
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
