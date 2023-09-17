package jpcompany.smartwire.window_desktop.controller;

import jpcompany.smartwire.window_desktop.dto.LogSaveDto;
import jpcompany.smartwire.window_desktop.service.LogService;
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


    // 로그인 정보가 맞으면 JWT 토큰 생성해서 헤더에 뿌려주면된다.
    @PostMapping("/api/login")
    public String post(@RequestBody String UserId) {
        log.info("api 전송받음 userId={}", UserId);
        return "ok" + UserId;
    }

    // JWT 인증을 기반으로 계정 정보가 넘어오면 이를 기반으로 DB, 클라이언트에 실시간으로 업데이트한다.
    @PostMapping("/api/log_test")
    public void realTimeUpdate(@RequestBody LogSaveDto logSaveDto) {
        log.info("받은 로그 정보={}", logSaveDto);
        logService.saveLog(logSaveDto);
        this.messagingTemplate.convertAndSend("/topic/logs/" + logSaveDto.getLoginId(), logSaveDto);
    }
}