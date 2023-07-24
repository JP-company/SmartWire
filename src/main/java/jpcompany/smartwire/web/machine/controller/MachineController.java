package jpcompany.smartwire.web.machine.controller;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.machine.dto.MachineDto;
import jpcompany.smartwire.web.machine.dto.MachineDtoList;
import jpcompany.smartwire.web.machine.repository.MachineRepositoryJdbcTemplate;
import jpcompany.smartwire.web.machine.service.MachineService;
import jpcompany.smartwire.web.member.controller.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MachineController {

    private final MachineService machineService;
    private final MachineRepositoryJdbcTemplate machineRepository;

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


    // TODO - machine id 값 클라이언트 노출
    @Transactional
    @PostMapping("/member/machine")
    public String postMachine(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                              @Valid @ModelAttribute MachineDtoList machineDtoList, BindingResult bindingResult,
                              RedirectAttributes redirectAttrs, Model model, HttpServletRequest request) {

        Set<String> set = new HashSet<>();
        if (machineDtoList.getMachines().stream().anyMatch(n -> !set.add(n.getMachineName()))) {
            bindingResult.reject("MachineNameDuplicated", "기계 이름은 중복될 수 없습니다.");
        }
        
        for (MachineDto machineDto : machineDtoList.getMachines()) {
            log.info("machineDto={}", machineDto);

            // 검증 로직
            if (bindingResult.hasErrors()) {
                log.info("errors = {}", bindingResult);
                model.addAttribute("member", loginMember);
                return "home/machine";
            }

            redirectAttrs.addFlashAttribute("popupMessage", "기계 설정이 완료되었습니다.");
            if (!machineService.saveMachineFormNHaveMachine(loginMember.getId(), loginMember.getHaveMachine(), machineDto)) {
                updateHaveMachineSession(request, true);
            }
        }
        return "redirect:/member/machine";
    }

    @PostMapping("/member/machine/delete")
    public String deleteMachine(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                @RequestParam Integer machineIdSend, @RequestParam String loginPassword,
                                RedirectAttributes redirectAttrs, HttpServletRequest request) {

        if (!loginPassword.equals(loginMember.getLoginPassword())) {
            redirectAttrs.addFlashAttribute("popupMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/member/machine";
        }

        // 기계가 하나도 없으면 DB, 세션 업데이트
        // TODO - Member 의 Setter 가 열려 있음
        if (!machineService.deleteMachineNHaveMachine(machineIdSend, loginMember.getId())) {
            updateHaveMachineSession(request, false);
        }

        log.info("정상 삭제={}", machineIdSend);
        redirectAttrs.addFlashAttribute("popupMessage", "기계 삭제에 성공하였습니다.");
        return "redirect:/member/machine";
    }

    // TODO - 새션 값 변경 동시성 문제
    private static void updateHaveMachineSession(HttpServletRequest request, boolean haveMachine) {
        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        member.setHaveMachine(haveMachine);
        log.info("새션 맴버={}",member);
    }
}
