package net.controller;

import net.model.RoomModel;
import net.model.UserModel;

import java.util.HashMap;

public class RoomManager {
    private static HashMap<Long, RoomModel> rooms = new HashMap<>();

    public static long createRoom (UserModel creator) {
        long newRoomNumer = RoomNumberManger.getRoomNumber();
        RoomModel newRoom = new RoomModel(creator, newRoomNumer);
        rooms.put(newRoomNumer, newRoom);
        return newRoomNumer;
    }

    public static RoomModel getRoom(long roomNumber) {
        return rooms.get(roomNumber);
    }
}
