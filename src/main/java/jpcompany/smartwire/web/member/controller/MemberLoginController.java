package jpcompany.smartwire.web.member.controller;

import jpcompany.smartwire.domain.Machine;
import jpcompany.smartwire.web.log.dto.LogDto;
import jpcompany.smartwire.web.log.repository.LogRepositoryJdbcTemplate;
import jpcompany.smartwire.web.machine.dto.MachineDto;
import jpcompany.smartwire.web.machine.repository.MachineRepositoryJdbcTemplate;
import jpcompany.smartwire.web.member.dto.MemberLoginDto;
import jpcompany.smartwire.web.member.dto.MemberResendEmailDto;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.member.service.MemberServiceEmail;
import jpcompany.smartwire.web.member.service.MemberServiceLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberLoginController {

    private final MemberServiceLogin serviceLogin;
    private final MemberServiceEmail memberServiceEmail;
    private final MachineRepositoryJdbcTemplate machineRepository;
    private final LogRepositoryJdbcTemplate logRepository;

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                       HttpServletRequest request, HttpServletResponse response, Model model) {
        // 임시 데이터 (삭제 해야함)
        model.addAttribute("memberLoginDto", new MemberLoginDto("wjsdj2009"));

        // 세션에 회원 데이터가 없으면 login 페이지로 이동
        log.info("loginMember={}",loginMember);
        if (loginMember == null) {
            return "home/login";
        }

        model.addAttribute("member", loginMember);
        // 이메일 미인증 계정
        if (!loginMember.getEmailVerified()) {
            return "home/email_verify";
        }

        // 세션 유지, 이메일 인증되었으면 메인페이지로 이동, 쿠키 유효기간 연장
        HttpSession session = request.getSession();
        Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
        sessionCookie.setMaxAge(432000);
        response.addCookie(sessionCookie);

        // 기계 설정 없는 화면
        if (!loginMember.getHaveMachine()) {
            return "home/main_no_machine";
        }

        // 정상 화면
        List<MachineDto> machines = machineRepository.findAll(loginMember.getId());
        List<Integer> machineIds = machines.stream()
                .map(MachineDto::getId)
                .collect(Collectors.toList());
        List<LogDto> recentLogAtEachMachineList = logRepository.getRecentLogAtEachMachine(machineIds);
        model.addAttribute("logDto", recentLogAtEachMachineList);
        return "home/main";
    }

    @PostMapping("/")
    public String login(@Validated @ModelAttribute(name = "memberLoginDto") MemberLoginDto memberLogin,
                        BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, Model model) {
        // 아이디 or 비밀번호 미입력
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "home/login";
        }

        Member loginMember = serviceLogin.login(memberLogin.getLoginId(), memberLogin.getLoginPassword());
        log.info("로그인 했을때={}", loginMember);

        // 로그인 실패 아이디 비밀번호 불일치
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "home/login";
        };

        // 이메일 미인증 계정
        if (!loginMember.getEmailVerified()) {
            model.addAttribute("member", loginMember);
            return "home/email_verify";
        }

        // 메일 인증 계정 로그인 성공 처리
        // 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        // 세션 ID를 쿠키로 전달, 쿠키 유효시간 설정
        Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
        sessionCookie.setMaxAge(432000);
        response.addCookie(sessionCookie);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 세션 삭제
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @PostMapping("/email_verify")
    public String resendMail(@Validated @ModelAttribute("member") MemberResendEmailDto member) throws MessagingException, UnsupportedEncodingException {
        String authCode = memberServiceEmail.sendEmail(member.getLoginId(), member.getEmail());
        serviceLogin.updateAuthCode(member.getLoginId(), authCode, member.getEmail());
        return "home/email_verify";
    }

    @GetMapping("/email_verify/{loginId}/{authKey}")
    public String verifyAuthMail(@PathVariable String loginId, @PathVariable String authKey, Model model) {
        Member member = serviceLogin.verifyAuthCode(loginId, authKey);
        if (member == null) {
            model.addAttribute("verified", "인증에 실패하였습니다. 홈페이지에서 인증 메일을 다시 요청해주세요");
            return "email/mail_complete";
        }
        model.addAttribute("verified", "인증에 성공하였습니다.");
        return "email/mail_complete";
    }
}
