package jpcompany.smartwire.web.member.controller.websocket;

import jpcompany.smartwire.window_desktop.controller.LogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketController {

    private SimpMessagingTemplate template;

    @Autowired
    public WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Scheduled(fixedDelay = 5000)
    public void updateLogInfo(LogDto logDto, Principal principal) {
        template.convertAndSendToUser(principal.getName(), "/topic/logs", logDto);
    }
}

