package am.ysu.encryptorsa.controller;

import am.ysu.encryptorsa.service.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final SessionManager sessionManager;

    @GetMapping
    public String startPage(HttpServletRequest request) {
        String username = ((String) request.getSession().getAttribute("username"));
        if (username != null) {
            sessionManager.removeUserSession(username);
        }
        request.getSession().invalidate();
        return "start";
    }

    @GetMapping("/{username}")
    public String mainPage(@PathVariable @NotNull String username, Model model, HttpServletResponse response) throws IOException {
        if (sessionManager.getUserSession(username) == null) {
            response.sendRedirect("");
        }
        model.addAttribute("username", username);
        return "main";
    }

    @GetMapping("/chat/{chatId}")
    public String messagePage(@PathVariable String chatId, Model model, @RequestParam("senderUsername") String senderUsername, @RequestParam("recipient") String recipient, @RequestParam("privateKey") String privateKey, HttpServletResponse response) throws IOException {
        if (sessionManager.getUserSession(senderUsername) == null) {
            response.sendRedirect("/");
        }
        model.addAttribute("chatId", chatId);
        model.addAttribute("username", senderUsername);
        model.addAttribute("recipient", String.valueOf(recipient.charAt(0)).toUpperCase().concat(recipient.substring(1)));
        model.addAttribute("privateKey", privateKey);
        return "messaging";
    }
}
