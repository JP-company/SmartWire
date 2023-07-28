package jpcompany.smartwire.window_desktop.log_receiver.controller;

import jpcompany.smartwire.window_desktop.log_receiver.dto.LogSaveDto;
import jpcompany.smartwire.window_desktop.log_receiver.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WindowController {

    private final SimpMessagingTemplate messagingTemplate;
    private final LogService logService;

    @PostMapping("/api/login")
    public String post(@RequestBody String UserId) {
        log.info("api 전송받음 userId={}", UserId);
        return "ok" + UserId;
    }

    @PostMapping("/api/log_test")
    public void realTimeUpdate(@RequestBody LogSaveDto logSaveDto) {
        log.info("받은 로그 정보={}", logSaveDto);
        logService.saveLog(logSaveDto);
        this.messagingTemplate.convertAndSend("/topic/logs/" + logSaveDto.getLoginId(), logSaveDto);
    }
}