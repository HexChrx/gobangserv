package net.model;

import java.util.LinkedList;
import java.util.List;

public class RoomModel {
    private long number;//房间号
    private List<UserModel> members;//房间成员
    private UserModel creator;//房间创建者
    private UserModel master;//房主
    private long ctime;//房间创建时间
    private String password;//房间密码
    private String name;// 房间名

    public RoomModel(UserModel creator, long number) {
        this.members = new LinkedList<>();
        this.ctime = System.currentTimeMillis();
        this.creator = creator;
        this.number = number;
    }

    public List<UserModel> getMembers() {
        return members;
    }

    public void setMembers(List<UserModel> members) {
        this.members = members;
    }

    public UserModel getCreator() {
        return creator;
    }

    public void setCreator(UserModel creator) {
        this.creator = creator;
    }

    public UserModel getMaster() {
        return master;
    }

    public void setMaster(UserModel master) {
        this.master = master;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

