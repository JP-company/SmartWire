package jpcompany.smartwire.web.member.controller;

import jpcompany.smartwire.web.log.dto.LogDto;
import jpcompany.smartwire.web.log.repository.LogRepositoryJdbcTemplate;
import jpcompany.smartwire.web.machine.dto.MachineDto;
import jpcompany.smartwire.web.machine.repository.MachineRepositoryJdbcTemplate;
import jpcompany.smartwire.web.member.auth.PrincipalDetails;
import jpcompany.smartwire.web.member.dto.MemberLoginDto;
import jpcompany.smartwire.web.member.dto.MemberResendEmailDto;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.member.service.MemberEmailService;
import jpcompany.smartwire.web.member.service.MemberLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberLoginController {

    private final MemberLoginService serviceLogin;
    private final MemberEmailService memberEmailService;
    private final MachineRepositoryJdbcTemplate machineRepository;
    private final LogRepositoryJdbcTemplate logRepository;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Member member = principalDetails.getMember();
        model.addAttribute("member", member);

        // 기계 설정 없는 화면
        if (!member.getHaveMachine()) {
            return "home/main_no_machine";
        }

        // 정상 화면
        List<MachineDto> machines = machineRepository.findAll(member.getId());
        List<Integer> machineIds = machines.stream()
                .map(MachineDto::getId)
                .collect(Collectors.toList());
        List<LogDto> recentLogAtEachMachineList = logRepository.getRecentLogAtEachMachine(machineIds);
        model.addAttribute("logDto", recentLogAtEachMachineList);
        return "home/main";
    }

    @GetMapping("/login")
    public String loginForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        if (principalDetails != null &&principalDetails.getMember() != null) {
            return "redirect:/";
        }
        model.addAttribute("memberLoginDto", new MemberLoginDto("wjsdj2008"));
        return "home/login";
    }

    @GetMapping("/email_verify")
    public String email(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        String loginId = (String) session.getAttribute("loginId");
        String email = (String) session.getAttribute("email");
        if (loginId == null || loginId.equals("")) {
            throw new RuntimeException("잘못된 접근입니다.");
        }
        MemberResendEmailDto memberResendEmailDto = new MemberResendEmailDto(loginId, email);
        model.addAttribute("member", memberResendEmailDto);
        return "home/email_verify";
    }

    @PostMapping("/email_verify")
    public String resendMail(@Validated @ModelAttribute("member") MemberResendEmailDto member, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        if (serviceLogin.isEmailVerified(member.getLoginId())) {
            throw new IllegalAccessError("잘못된 접근입니다.");
        }

        String authToken = memberEmailService.sendEmail(member.getLoginId(), member.getEmail());
        serviceLogin.updateAuthToken(member.getLoginId(), authToken, member.getEmail());

        // 세션 업데이트
        HttpSession session = request.getSession();
        session.setAttribute("loginId", member.getLoginId());
        session.setAttribute("email", member.getEmail());
        return "redirect:/email_verify";
    }

    @GetMapping("/email_verify/{authToken}")
    public String verifyAuthMail(@PathVariable String authToken, Model model) {
        Member member = serviceLogin.verifyAuthToken(authToken);
        if (member == null) {
            model.addAttribute("verified", "인증에 실패하였습니다. 홈페이지에서 인증 메일을 다시 요청해주세요");
            return "email/mail_complete";
        }
        model.addAttribute("verified", "인증에 성공하였습니다.");
        return "email/mail_complete";
    }
}
