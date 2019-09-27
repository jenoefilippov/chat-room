package edu.udacity.java.nano.chat.model;

/**
 * WebSocket message model
 */
public class Message {

    private MessageType messageType;
    private String username;
    private String msg;
    private int onlineCount;

    public enum MessageType {
        ENTER,
        CHAT,
        LEAVE
    }

    public Message() {}

    public Message(MessageType messageType, String username, String msg) {
        this.messageType = messageType;
        this.username = username;
        this.msg = msg;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }
}
