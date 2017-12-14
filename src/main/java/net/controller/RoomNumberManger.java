package net.controller;

public final class RoomNumberManger {
    private static long number = (long) 1;
    public synchronized static long getRoomNumber() {
        number = number + 1;
        return number;
    }
}
