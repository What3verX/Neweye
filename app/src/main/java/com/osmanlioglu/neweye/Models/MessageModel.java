package com.osmanlioglu.neweye.Models;

public class MessageModel {
    String uId, message, messageId, userName, imageUrl;
    Long timestamp;

    public MessageModel(String uId, String message, Long timestamp, String userName, String imageUrl) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

    public MessageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
        this.timestamp = null;
        this.userName = "Anonymous";
        this.imageUrl = null;
    }

    public MessageModel() {}

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserImage(String userImage) {
        this.imageUrl=userImage;
    }

    public String getSenderId() {
        return uId;
    }
}





