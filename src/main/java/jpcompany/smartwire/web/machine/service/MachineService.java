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
        Machine machine = new Machine();
        machine.setId(machineDto.getId());
        machine.setMachineName(machineDto.getMachineName());
        machine.setMachineModel(machineDto.getMachineModel());
        machine.setDateManufacture(machineDto.getDateManufacture());
        machine.setMemberId(memberId);
        machine.setSequence(machineDto.getSequence());

        if (machineDto.getId() == null) {
            machineRepository.save(machine);
        } else {
            machineRepository.updateInformation(machineDto);
        }

        log.info("추가 할떄 기계 있나요={}", haveMachine);
        // 기게 정보가 없으면 세션 업데이트를 위해 false 반환 -> 기게있는 페이지를 메인에서 보여줘야하니까
        if (!haveMachine) {
            memberRepository.updateHaveMachine(memberId, true);
            return false;
        }

        return true;
    }

    public Boolean deleteMachineNHaveMachine(Integer machineIdSend, Integer memberId) {
        machineRepository.delete(machineIdSend);
        log.info("기계 얼마나 남았니={}", machineRepository.findAll(memberId).size());
        if (machineRepository.findAll(memberId).size() == 0) {
            memberRepository.updateHaveMachine(memberId, false);
            return false;
        }
        return true;
    }
}
