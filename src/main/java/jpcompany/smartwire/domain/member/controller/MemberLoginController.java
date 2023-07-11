package jpcompany.smartwire.domain.member.controller;

import jpcompany.smartwire.domain.member.*;
import jpcompany.smartwire.domain.member.service.account.MemberServiceAccount;
import jpcompany.smartwire.domain.member.service.email.EmailService;
import jpcompany.smartwire.domain.member.service.login.MemberServiceLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberLoginController {

    private final MemberServiceLogin serviceLogin;
    private final EmailService emailService;
    private final MemberServiceAccount serviceAccount;

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) throws MessagingException, UnsupportedEncodingException {
        model.addAttribute("memberLoginForm", new MemberLoginForm("gmwire4485"));
        // 세션에 회원 데이터가 없으면 home 으로 이동
        if (loginMember == null) {
            return "home/login";
        }
        model.addAttribute("member", loginMember);
        log.info("loginMember={}", loginMember);

        if (!loginMember.getEmailVerified()) {
            if (loginMember.getAuthCode() == null) {
                String authCode = emailService.sendEmail(loginMember.getLoginId(), loginMember.getEmail());
                serviceAccount.updateAuthCode(loginMember.getLoginId(), authCode, loginMember.getEmail());
            }
            return "home/email_verify";
        }
        // 세션 유지, 이메일 인증되었으면 로그인으로 이동
        return "home/main";
    }

    @PostMapping("/")
    public String login(@Validated @ModelAttribute(name = "memberLoginForm") MemberLoginForm memberLogin,
                        BindingResult bindingResult, HttpServletRequest request) {
        // 로그인 폼 오류 처리
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "home/login";
        }

        // 로그인 실패 처리
        Member loginMember = serviceLogin.login(memberLogin.getLoginId(), memberLogin.getLoginPassword());
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "home/login";
        };

        // 로그인 성공 처리
        // 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

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

    @PostMapping("/mail-verify")
    public String resendMail(@Validated @ModelAttribute("member") MemberResendEmailForm member) throws MessagingException, UnsupportedEncodingException {
        String authCode = emailService.sendEmail(member.getLoginId(), member.getEmail());
        serviceAccount.updateAuthCode(member.getLoginId(), authCode, member.getEmail());
        return "home/email_verify";
    }

    @GetMapping("/{loginId}/{authKey}/email_verify")
    public String verifyAuthMail(@PathVariable String loginId, @PathVariable String authKey, Model model) {
        Member member = serviceAccount.verifyAuthCode(loginId, authKey);
        if (member == null) {
            model.addAttribute("verified", "인증에 실패하였습니다. 홈페이지에서 인증 메일을 다시 요청해주세요");
            return "mail_complete";
        }
        model.addAttribute("verified", "인증에 성공하였습니다.");
        return "mail_complete";
    }
}
