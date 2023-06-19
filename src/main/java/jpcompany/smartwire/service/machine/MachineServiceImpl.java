package jpcompany.smartwire.service.machine;

import jpcompany.smartwire.domain.Machine;
import jpcompany.smartwire.repository.machine.MachineRepository;
import jpcompany.smartwire.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineServiceImpl implements MachineService{


    MachineRepository machineRepository;

    @Autowired
    public MachineServiceImpl(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }
    @Override
    public Machine addMachine(Machine machine, Long memberId) {
        machine.setMemberId(memberId);
        machineRepository.save(machine);
        return machine;
    }

    @Override
    public void updateMachine(Machine machine) {
        machineRepository.update(machine);
    }

    @Override
    public void deleteMachine() {

    }

    @Override
    public List<Machine> findAllMachines(Long memberId) {
        return machineRepository.findAllMachinesByMemberId(memberId);
    }
}
