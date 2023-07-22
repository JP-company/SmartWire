package jpcompany.smartwire.web.member.controller;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.machine.service.MachineService;
import jpcompany.smartwire.web.member.SessionConst;
import jpcompany.smartwire.web.member.dto.MemberUpdateDto;
import jpcompany.smartwire.web.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    private final MachineService machineService;

    @GetMapping("/member")
    public String member(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        model.addAttribute("member", loginMember);
        return "home/member";
    }

    @PostMapping("/member")
    public String postMember(@ModelAttribute(name = "memberUpdateDto") MemberUpdateDto memberUpdateDto, HttpServletRequest request) {
        Member member = memberService.updateCompanyNameNPhoneNumber(memberUpdateDto);
        log.info("memberUpdateDto={}", memberUpdateDto);
        HttpSession session = request.getSession(false);
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);
        return "redirect:/member";
    }


}
