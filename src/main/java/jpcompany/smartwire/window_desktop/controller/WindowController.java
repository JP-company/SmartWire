package jpcompany.smartwire.window_desktop.controller;

import jpcompany.smartwire.web.member.dto.MemberLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WindowController {

    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/api/login")
    public String post(@RequestBody String UserId) {
        log.info("api 전송받음 userId={}", UserId);
        return "ok" + UserId;
    }

    @PostMapping("/api/log_test")
    public void realTimeUpdate(@RequestBody LogDto logDto) {
        String companyId = "wjsdj2009";
        this.messagingTemplate.convertAndSend("/topic/logs/" + companyId, logDto);


    }
}