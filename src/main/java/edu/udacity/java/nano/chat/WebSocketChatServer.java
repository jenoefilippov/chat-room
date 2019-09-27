package edu.udacity.java.nano.chat;

import edu.udacity.java.nano.chat.cofig.MessageDecoder;
import edu.udacity.java.nano.chat.cofig.MessageEncoder;
import edu.udacity.java.nano.chat.model.Message;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */
@Component
@ServerEndpoint(
        value = "/chat/{username}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class
)
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(Message message) {
        sendMessageToAll(message.getMsg(), message.getMessageType(), message.getUsername());
    }

    private static void sendMessageToAll(String msg, Message.MessageType messageType, String sender) {
        Message message = new Message();
        message.setMsg(msg);
        message.setMessageType(messageType);
        message.setUsername(sender);
        message.setOnlineCount(onlineSessions.size());

        onlineSessions.values().forEach(session -> {
            try {
                session.getBasicRemote().sendObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("username") String username
    ) {
        onlineSessions.put(username, session);

        String msg = "User " + username + " joined!";
        sendMessageToAll(msg, Message.MessageType.ENTER, username);
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        MessageDecoder decoder = new MessageDecoder();

        try {
            Message message = decoder.decode(jsonStr);
            message.setMessageType(Message.MessageType.CHAT);
            sendMessageToAll(message);
        } catch (DecodeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(
            Session session,
            @PathParam("username") String username
    ) {
        onlineSessions.remove(username);

        String msg = "User " + username + " left!";
        sendMessageToAll(msg, Message.MessageType.LEAVE, username);
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}