package jpcompany.smartwire.window_desktop.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.firebase.dto.FCMNotificationDto;
import jpcompany.smartwire.firebase.service.FCMNotificationService;
import jpcompany.smartwire.security.jwt.handler.dto.JwtAuthenticationDto;
import jpcompany.smartwire.security.jwt.handler.dto.JwtMemberDto;
import jpcompany.smartwire.web.machine.dto.MachineDto;
import jpcompany.smartwire.web.machine.repository.MachineRepositoryJdbcTemplate;
import jpcompany.smartwire.window_desktop.dto.LogSaveDto;
import jpcompany.smartwire.window_desktop.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WindowController {

    private final SimpMessagingTemplate messagingTemplate;
    private final LogService logService;
    private final ObjectMapper objectMapper;
    private final MachineRepositoryJdbcTemplate machineRepository;
    private final FCMNotificationService fcmNotificationService;

    @GetMapping("/api/auto_login")
    public void autoLogin(HttpServletResponse response) throws IOException {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 없는 필드 무시

        JwtMemberDto jwtMemberDto = objectMapper.convertValue(member, JwtMemberDto.class);
        List<MachineDto> machineList = machineRepository.findAll(member.getId());
        machineList.sort(Comparator.comparingInt(MachineDto::getSequence));

        String jwtAuthenticationJson = objectMapper.writeValueAsString(new JwtAuthenticationDto(jwtMemberDto, machineList));

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(jwtAuthenticationJson);
    }

    @PostMapping("/api/log_test")
    public String realTimeUpdate(@RequestBody LogSaveDto logSaveDto) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logService.saveLog(logSaveDto);
        messagingTemplate.convertAndSend("/topic/logs/" + member.getLoginId(), logSaveDto);

        String machineName = machineRepository
                .findMachineNameById(logSaveDto.getMachineId())
                .orElse("");
        String log = logSaveDto.getLog().split("_")[1];
        String message = Objects.equals(log, "작업 시작") ? log + " " + logSaveDto.getFile() : log;
        fcmNotificationService.sendNotificationByToken(
                new FCMNotificationDto(member.getId(), machineName + " 상태 변경 알림", message), member
        );
        return "Log send complete";
    }
}
