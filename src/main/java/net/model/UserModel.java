package net.model;

import net.socket.Session;

public class UserModel {
    private String uid;
    private String name;
    private Session session;

    public UserModel() {}

    public UserModel(String uid, String name, Session session) {
        this.uid = uid;
        this.name = name;
        this.session = session;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Session getSession() {
        return this.session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
