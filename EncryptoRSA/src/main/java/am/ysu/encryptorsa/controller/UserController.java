package am.ysu.encryptorsa.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import am.ysu.encryptorsa.service.SessionManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final SessionManager sessionManager;


    @ResponseBody
    @PostMapping("/login")
    public boolean startChattingByUsername(@RequestParam(value = "username") String username, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        if (sessionManager.usernameNotLoggedIn(username)) {
            session.setAttribute("username", username);
            sessionManager.addUserSession(username, session);
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/find-user")
    public ResponseEntity<String> findUserForChatting(@RequestParam("recipient") String recipient, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        if (username == null) return ResponseEntity.notFound().build();
        if (sessionManager.usernameNotLoggedIn(recipient) || username.equals(recipient))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid recipient!");
        String chatId = (String) sessionManager.getUserSession(recipient).getAttribute("chatId");
        if (chatId == null) {
            chatId = UUID.randomUUID().toString();
        }
        request.getSession().setAttribute("chatId", chatId);
        request.getSession().setAttribute("username", username);
        request.getSession().setAttribute("recipient", recipient);
        sessionManager.addUserSession(username, request.getSession());
        JsonNode node = new ObjectMapper().createObjectNode().put("chatId", chatId).put("senderUsername", username).put("recipient", recipient);
        return ResponseEntity.ok(node.toPrettyString());
    }

}
