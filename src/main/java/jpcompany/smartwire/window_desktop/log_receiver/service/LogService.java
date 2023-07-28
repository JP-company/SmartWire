package jpcompany.smartwire.window_desktop.log_receiver.service;

import jpcompany.smartwire.domain.Date;
import jpcompany.smartwire.domain.Log;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.domain.Process;
import jpcompany.smartwire.web.machine.repository.MachineRepositoryJdbcTemplate;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import jpcompany.smartwire.window_desktop.log_receiver.dto.LogSaveDto;
import jpcompany.smartwire.window_desktop.log_receiver.repository.LogReceiverJdbcTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LogService {
    private final LogReceiverJdbcTemplateRepository logReceiverRepository;
    private final MemberRepository memberRepository;
    private final MachineRepositoryJdbcTemplate machineRepository;

    public void saveLog(LogSaveDto logSaveDto) {
        Member member = memberRepository.findByLoginId(logSaveDto.getLoginId()).get();
        Integer machineId = machineRepository.findByMemberIdNMachineName(member.getId(), logSaveDto.getMachineName()).get();
        Integer machineDateId = logReceiverRepository.findRecentDateIdByMachineId(machineId).get();
        log.info("machineDateId 처음에={}", machineDateId);

        if (logSaveDto.getDate() != null) {
            Date date = new Date();
            date.setDate(logSaveDto.getDate());
            date.setMachineId(machineId);
            log.info("날짜={}", date);

            machineDateId = logReceiverRepository.saveDate(date).getId();
            log.info("기계 날짜 id={}", machineDateId);
        }

        log.info("machineDateId={}", machineDateId);
        Integer processId = logReceiverRepository.findRecentProcessIdByMachineDateId(machineDateId).orElse(null);
        if (logSaveDto.getLog().split("_")[0].equals("start")) {
            Process processDto = new Process();
            processDto.setFile(logSaveDto.getFile());
            processDto.setThickness(logSaveDto.getThickness());
            processDto.setStartedTime(logSaveDto.getLogTime());
            processDto.setMachineDateId(machineDateId);
            processId = logReceiverRepository.saveProcess(processDto).getId();
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
