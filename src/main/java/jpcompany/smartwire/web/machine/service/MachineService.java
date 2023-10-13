package jpcompany.smartwire.web.machine.service;

import jpcompany.smartwire.domain.Machine;
import jpcompany.smartwire.web.machine.dto.MachineDto;
import jpcompany.smartwire.web.machine.repository.MachineRepositoryJdbcTemplate;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MachineService {

    private final MemberRepository memberRepository;
    private final MachineRepositoryJdbcTemplate machineRepository;

    public Boolean saveMachineFormNHaveMachine(Integer memberId, Boolean haveMachine, MachineDto machineDto) {
        machineDto.setMemberId(memberId);

        if (machineDto.getId() == null) {
            machineRepository.save(machineDto);
        } else {
            machineRepository.updateInformation(machineDto);
        }

        // 기게 정보가 없으면 세션 업데이트를 위해 false 반환 -> 기게있는 페이지를 메인에서 보여줘야하니까
        if (!haveMachine) {
            memberRepository.updateHaveMachine(memberId, true);
            return false;
        }

        return true;
    }

    public Boolean deleteMachineNHaveMachine(Integer machineIdSend, Integer memberId) {
        machineRepository.delete(machineIdSend);
        if (machineRepository.findAll(memberId).size() == 0) {
            memberRepository.updateHaveMachine(memberId, false);
            return false;
        }
        return true;
    }
}
