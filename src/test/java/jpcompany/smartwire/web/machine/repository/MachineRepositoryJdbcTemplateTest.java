package jpcompany.smartwire.web.machine.repository;


import jpcompany.smartwire.domain.Machine;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.log_view.repository.LogRepositoryJdbcTemplate;
import jpcompany.smartwire.web.machine.dto.MachineDto;
import jpcompany.smartwire.web.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
//@Transactional
class MachineRepositoryJdbcTemplateTest {

    @Autowired
    private MachineRepositoryJdbcTemplate machineRepository;
    @Autowired
    private MemberRepository repository;

    @Autowired
    private LogRepositoryJdbcTemplate logRepository;

    @Test
    void save() {
        Member member = new Member("gm12gm", "1234", "에스아이티",
                "wjsdj2008@naver.com", "010-8714-4246", true,
                false, "as23121d");
        Member savedMember = repository.save(member);

        List<Machine> listMachines = new ArrayList<>();
        Machine machine1 = new Machine("1호기", "1", LocalDate.now(), 0, false, savedMember.getId());
        Machine machine2 = new Machine("2호기", "2", LocalDate.of(2013, 4, 12), 0, false, savedMember.getId());
        Machine machine3 = new Machine("3호기", "3", LocalDate.of(2004, 1, 31), 0, false, savedMember.getId());
        listMachines.add(machine1);
        listMachines.add(machine2);
        listMachines.add(machine3);
        for (Machine listMachine : listMachines) {
            machineRepository.save(listMachine);
        }

        List<MachineDto> machines = machineRepository.findAll(savedMember.getId());
        log.info("machines={}", machines);
        assertThat(machines.size()).isEqualTo(3);
    }

    @Test
    void uploadInformation() {
        List<Machine> listMachines = new ArrayList<>();
        Machine machine1 = new Machine("1호기", "1", LocalDate.now(), 0, false, 171);
        Machine machine2 = new Machine("2호기", "2", LocalDate.of(2013, 4, 12), 0, false, 171);
        Machine machine3 = new Machine("3호기", "3", LocalDate.of(2004, 1, 31), 0, false, 171);
        listMachines.add(machine1);
        listMachines.add(machine2);
        listMachines.add(machine3);
        for (Machine listMachine : listMachines) {
            machineRepository.save(listMachine);
        }
    }

    @Test
    @DisplayName("기계 없을 때 리스트 불러오기")
    void findAllWhenNone() {
        List<MachineDto> all = machineRepository.findAll(-1);
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    void delete() {
//        machineRepository.delete(44);
    }
}