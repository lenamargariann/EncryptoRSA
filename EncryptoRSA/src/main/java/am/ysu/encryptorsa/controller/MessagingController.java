package am.ysu.encryptorsa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessagingController {


    @MessageMapping("/{chatId}")
    @SendTo("/topic/{chatId}")
    public String send(String message, @DestinationVariable("chatId") String chatId) {
        log.info("ChatID: " + chatId);
        log.info("Sent Message: " + message);
        return message;
    }

}
