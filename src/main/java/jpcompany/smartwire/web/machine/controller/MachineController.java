package jpcompany.smartwire.web.machine.controller;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.machine.dto.MachineDto;
import jpcompany.smartwire.web.machine.dto.MachineDtoList;
import jpcompany.smartwire.web.machine.dto.MachineDtoValidator;
import jpcompany.smartwire.web.machine.repository.MachineRepositoryJdbcTemplate;
import jpcompany.smartwire.web.machine.service.MachineService;
import jpcompany.smartwire.web.member.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MachineController {

    private final MachineService machineService;
    private final MachineRepositoryJdbcTemplate machineRepository;
    private final MachineDtoValidator machineDtoValidator;

    @GetMapping("/member/machine")
    public String machine(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                          Model model) {
        model.addAttribute("member", loginMember);
        List<MachineDto> machines = machineRepository.findAll(loginMember.getId());
        machines.sort(Comparator.comparingInt(MachineDto::getSequence));
        log.info("machines={}", machines);
        model.addAttribute("machineDtoList", new MachineDtoList(machines));
        return "home/machine";
    }

    @Transactional
    @PostMapping("/member/machine")
    public String postMachine(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                              @Valid @ModelAttribute MachineDtoList machineDtoList, BindingResult bindingResult, Model model) {

        for (MachineDto machineDto : machineDtoList.getMachines()) {
            log.info("machineDto={}", machineDto);

            // 검증 로직
            if (bindingResult.hasErrors()) {
                log.info("errors = {}", bindingResult);
                model.addAttribute("member", loginMember);
                return "home/machine";
            }

            machineService.saveMachineForm(loginMember.getId(), machineDto);
        }
        return "redirect:/member/machine";
    }


}
