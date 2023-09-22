package jpcompany.smartwire.window_desktop.service;

import jpcompany.smartwire.domain.Date;
import jpcompany.smartwire.domain.Log;
import jpcompany.smartwire.domain.Process;
import jpcompany.smartwire.web.machine.repository.MachineRepositoryJdbcTemplate;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import jpcompany.smartwire.window_desktop.dto.LogSaveDto;
import jpcompany.smartwire.window_desktop.repository.LogReceiverJdbcTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LogService {
    private final LogReceiverJdbcTemplateRepository logReceiverRepository;
    private final MemberRepository memberRepository;
    private final MachineRepositoryJdbcTemplate machineRepository;

    public void saveLog(LogSaveDto logSaveDto) {
        // 윈도우한테 받은 데이터에서 회원 정보 추출
//        Member member = memberRepository.findByLoginId(logSaveDto.getLoginId()).get();

        // 회원 id, 기계 이름을 가지고 기계 id 추출
//        Integer machineId = machineRepository.findByMemberIdNMachineName(member.getId(), logSaveDto.getMachineName()).get();
        Integer machineId = logSaveDto.getMachineId();

        // 기계 id를 가지고 최근 날짜 id 추출
        Date recentDateDto = logReceiverRepository.findRecentDateByMachineId(machineId).orElse(null);

        Integer machineDateId;
        LocalDate recentDate;

        if (recentDateDto != null) {  // 최근날짜가 있으면
            machineDateId = recentDateDto.getId();
            recentDate = recentDateDto.getDate();
            log.info("machineDateId 처음에={}", machineDateId);
        } else {  // 최근날짜가 없으면
            recentDate = LocalDate.now();
            Date date = new Date();
            date.setDate(recentDate);
            date.setMachineId(machineId);
            log.info("날짜={}", date);
            Date date1 = logReceiverRepository.saveDate(date);
            machineDateId = date1.getId();
        }

        // 데이터에 날짜가 있으면 새로운 날짜 생성, 없으면 이전과 같은 날짜
        if (logSaveDto.getDate() != null) {
            Date date = new Date();
            date.setDate(logSaveDto.getDate());
            date.setMachineId(machineId);
            log.info("날짜={}", date);

            Date date1 = logReceiverRepository.saveDate(date);
            machineDateId = date1.getId();
            recentDate = date1.getDate();
            log.info("기계 날짜 id={}", machineDateId);
        }

        // 기계 날짜 id를 가지고 최근 작업 id 추출
        log.info("machineDateId={}", machineDateId);
        Process process = logReceiverRepository.findRecentProcessByMachineId(machineId).orElse(null);
        Integer processId = null;
        // 작업이 없지 않고, 최근 작업이 끝난게 아니면 최근 작업으로 설정
        if (process != null && process.getFinishedTime() == null) {
            processId = process.getId();
        }

        // 로그가 작업 시작이면, 새로운 작업 생성, reset 이면 작업 종료
        if (logSaveDto.getLog().split("_")[0].equals("start")) {
            Process processDto = new Process();
            processDto.setFile(logSaveDto.getFile());
            processDto.setThickness(logSaveDto.getThickness());
            processDto.setStartedTime(logSaveDto.getLogTime());
            processDto.setMachineDateId(machineDateId);
            processDto.setMachineId(machineId);
            processId = logReceiverRepository.saveProcess(processDto).getId();
        } else if (processId != null && logSaveDto.getLog().split("_")[0].equals("reset")) {
            logReceiverRepository.doneProcess(processId,
                    LocalDateTime.of(recentDate, logSaveDto.getLogTime()),
                    logSaveDto.getActualProcessTime());
        }

        Log log = new Log();
        log.setLog(logSaveDto.getLog());
        log.setLogTime(logSaveDto.getLogTime());
        log.setProcessId(processId);
        log.setMachineDateId(machineDateId);
        log.setMachineId(machineId);
        logReceiverRepository.saveLog(log);
    }
}