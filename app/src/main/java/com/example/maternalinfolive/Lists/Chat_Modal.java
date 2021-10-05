package com.example.maternalinfolive.Lists;

public class Chat_Modal {
    String message, sender, receiver, msg_id, date_sent,file_path;

    public Chat_Modal() {
    }

    public Chat_Modal(String message, String sender, String receiver, String msg_id, String date_sent, String file_path) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.msg_id = msg_id;
        this.date_sent = date_sent;
        this.file_path = file_path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(String date_sent) {
        this.date_sent = date_sent;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}