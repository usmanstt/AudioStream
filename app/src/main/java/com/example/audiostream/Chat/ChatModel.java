package com.example.audiostream.Chat;

public class ChatModel {
    String id,sendermessage,senderid;
    long timeStamp;
    public ChatModel()
    {

    }
    public ChatModel (String message,String id,String senderid,long timeStamp)
    {
        this.id=id;
        this.sendermessage=message;
        this.senderid=senderid;
        this.timeStamp=timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSendermessage() {
        return sendermessage;
    }

    public void setSendermessage(String sendermessage) {
        this.sendermessage = sendermessage;
    }
}
