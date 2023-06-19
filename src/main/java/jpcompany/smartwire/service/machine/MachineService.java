package jpcompany.smartwire.service.machine;

import jpcompany.smartwire.domain.Machine;

import java.util.List;

public interface MachineService {
    Machine addMachine(Machine machine, Long memberId);
    void updateMachine(Machine machine);
    void deleteMachine();
    List<Machine> findAllMachines(Long memberId);

}
