package jpcompany.smartwire.web.machine.service;

import jpcompany.smartwire.domain.Machine;
import jpcompany.smartwire.web.machine.dto.MachineDto;
import jpcompany.smartwire.web.machine.repository.MachineRepositoryJdbcTemplate;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MachineService {

    private final MemberRepository memberRepository;


    private final MachineRepositoryJdbcTemplate machineRepository;

    public void saveMachineForm(Integer memberId, MachineDto machineDto) {
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
    }
}
