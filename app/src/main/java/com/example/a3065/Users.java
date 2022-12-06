package com.example.a3065;

import android.net.Uri;
import android.util.Log;

public class Users {
    public String name,major,uid,username,accAge,email,about;
    public Uri imge;
    public Object images,following;
    public Users(){

    }
    public Users(Object obj, Object following){
        this.images=obj;
        this.following=following;

    }
    public Users(String name,String major,String uid,String accAge, String username, String email,String about) {
        this.name=name;//
        this.major=major;//
        this.uid=uid;//
        this.email=email;//
        this.accAge=accAge;//
        this.username=username;
        this.about=about;

    }

    public Object getFollow() {
        return following;
    }

    public String getAccAge(){
        return accAge;
    }
    public Uri getImg(){
        return imge;
    }
    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return this.email;
    }

    public String getUID() {
        return uid;
    }
    public String getName() {
        return name;
    }
    public String getMajor() {
        return major;
    }
    public void setImg(String img){
        this.imge= Uri.parse(img);

    }
    public Uri getImgs(){
        return imge;
    }

}

