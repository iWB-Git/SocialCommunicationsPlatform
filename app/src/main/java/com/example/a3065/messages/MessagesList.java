package com.example.a3065.messages;

public class MessagesList {
    private String name,uid,lastMessage,profilePic;

    public MessagesList(String name, String uid, String lastMessage,String profilePic, int unseenMessages) {
        this.name = name;
        this.uid = uid;
        this.profilePic=profilePic;
        this.lastMessage = lastMessage;
        this.unseenMessages = unseenMessages;
    }


    public String getProfilePic() {
        return profilePic;

    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUnseenMessages(int unseenMessages) {
        this.unseenMessages = unseenMessages;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    private int unseenMessages;
}
