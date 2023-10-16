package jpcompany.smartwire.mobile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.mobile.dto.FcmTokenAndAlarmSettingDto;
import jpcompany.smartwire.mobile.service.MobileService;
import jpcompany.smartwire.web.log_view.dto.LogVIewDto;
import jpcompany.smartwire.web.log_view.repository.LogRepositoryJdbcTemplate;
import jpcompany.smartwire.web.machine.dto.MachineDto;
import jpcompany.smartwire.web.machine.repository.MachineRepositoryJdbcTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MobileController {

    private final MachineRepositoryJdbcTemplate machineRepository;
    private final LogRepositoryJdbcTemplate logRepository;
    private final ObjectMapper objectMapper;
    private final MobileService mobileService;

    @GetMapping("/api/home")
    public void apiHome(HttpServletResponse response) throws IOException {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<MachineDto> machines = machineRepository.findAll(member.getId());
        machines.sort(Comparator.comparingInt(MachineDto::getSequence));
        List<Integer> machineIds = machines.stream()
                .map(MachineDto::getId)
                .collect(Collectors.toList());

        List<LogVIewDto> recentLogAtEachMachineList = logRepository.getRecentLogAtEachMachine(machineIds);
        List<String> machineNameWhoHasLog = recentLogAtEachMachineList.stream()
                .map(LogVIewDto::getMachineName)
                .collect(Collectors.toList());

        machines.stream().filter(machine -> !machineNameWhoHasLog.contains(machine.getMachineName()))
                .forEach(machine -> recentLogAtEachMachineList.add(new LogVIewDto(machine.getMachineName(), machine.getSequence())));

        recentLogAtEachMachineList.sort(Comparator.comparingInt(LogVIewDto::getSequence));

        String jwtAuthenticationJson = objectMapper.writeValueAsString(recentLogAtEachMachineList);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(jwtAuthenticationJson);
    }

    @PostMapping("/api/fcm_token")
    public String getFCMToken(@RequestBody FcmTokenAndAlarmSettingDto fcmTokenAndAlarmSettingDto) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        fcmTokenAndAlarmSettingDto.setMemberId(member.getId());
        mobileService.saveFcmTokenAndAlarmSettingAtDB(fcmTokenAndAlarmSettingDto);
        return "fcmToken and AlarmSetting saved complete";
    }
}













