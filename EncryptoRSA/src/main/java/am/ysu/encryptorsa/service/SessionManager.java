package am.ysu.encryptorsa.service;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class SessionManager {

    private final Map<String, HttpSession> userSessions = new HashMap<>();

    public void addUserSession(String username, HttpSession session) {
        log.info("Added user session by username: " + username);
        userSessions.put(username, session);
    }

    public HttpSession getUserSession(String username) {
        HttpSession s = userSessions.get(username);
        if (s == null || (s.getCreationTime() + s.getMaxInactiveInterval() * 1000L < System.currentTimeMillis()))
            return null;
        return s;
    }

    public void removeUserSession(String username) {
        userSessions.remove(username);
    }

    public boolean usernameNotLoggedIn(String username) {
        return !userSessions.containsKey(username);
    }

}

