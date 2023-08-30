package com.ii.testautomation.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ProgressWebSocketHandler  implements WebSocketHandler {
    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void broadcastProgress(int progress) {
        TextMessage message = new TextMessage(String.valueOf(progress));
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(message);
            } catch (IOException e) {

            }
        }
    }
}
