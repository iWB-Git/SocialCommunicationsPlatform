package com.example.a3065;

public class AdminVerify {
    String uid, email, name;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public AdminVerify(String uid, String name, String email) {
        this.uid = uid;
        this.email = email;
        this.name = name;
    }
}
