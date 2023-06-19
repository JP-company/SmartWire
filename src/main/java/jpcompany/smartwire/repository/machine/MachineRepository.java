package jpcompany.smartwire.repository.machine;

import jpcompany.smartwire.domain.Machine;
import jpcompany.smartwire.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MachineRepository {
    Machine save(Machine machine);
    Machine update(Machine machine);
    Optional<Machine> findById(Long id);
    List<Machine> findAllMachinesByMemberId(Long memberId);
}