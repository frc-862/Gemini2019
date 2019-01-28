package frc.lightning.http;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

@WebSocket
public class DataLoggingWebSocket {
    // Store sessions if you want to, for example, broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        System.out.println("Websocket message: " + message);   // Print message
        session.getRemote().sendString(message); // and send it back
    }

    public static void broadcast(String message) {
        sessions.stream().filter(Session::isOpen).forEach((session) -> {
            try {
                session.getRemote().sendString(message);
            } catch (IOException e) {
                System.err.println("Error writing to websocket logging client: " + e);
            }
        });
    }

    public static boolean isActive() {
        return sessions.stream().anyMatch(Session::isOpen);
    }
}
