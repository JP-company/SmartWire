package jpcompany.smartwire.web.machine.service;

import jpcompany.smartwire.domain.Machine;
import jpcompany.smartwire.web.machine.dto.MachineSaveDto;
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

    public Machine saveMachineInfo(Integer memberId, MachineSaveDto machineSaveDto) {
        Machine machine = new Machine();
        machine.setMachineName(machineSaveDto.getMachineName());
        machine.setMachineModel(machineSaveDto.getMachineModel());
        machine.setDateManufacture(machineSaveDto.getDateManufacture());
        machine.setMemberId(memberId);
        return machineRepository.save(machine);
    }
}
