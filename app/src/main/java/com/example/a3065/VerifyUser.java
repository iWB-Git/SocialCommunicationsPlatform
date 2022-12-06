package com.example.a3065;

public class VerifyUser
{
    String uid,email,name;

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
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

    public VerifyUser(String uid, String email, String name){
        this.uid=uid;
        this.email=email;
        this.name=name;

    }
}
