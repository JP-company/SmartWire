package jpcompany.smartwire.web.member.controller;

import jpcompany.smartwire.domain.member.Member;
import jpcompany.smartwire.web.member.SessionConst;
import jpcompany.smartwire.web.member.dto.MemberJoinDto;
import jpcompany.smartwire.web.member.service.MemberServiceJoin;
import jpcompany.smartwire.web.member.service.MemberServiceLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberJoinController {
    private final MemberServiceJoin serviceJoin;
    private final MemberServiceLogin serviceLogin;

    @GetMapping("/join")
    public String join(Model model) {
//        model.addAttribute("memberJoinForm", new MemberJoinForm());
        model.addAttribute("memberJoinForm", initMemberJoinForm());
        return "home/join";
    }

    @PostMapping("/join")
    public String joinIn(@Validated @ModelAttribute(name = "memberJoinForm") MemberJoinDto memberJoinDto, BindingResult bindingResult, HttpServletRequest request) {

        // 검증 로직
        if(!serviceJoin.passwordDoubleCheck(memberJoinDto.getLoginPassword(), memberJoinDto.getLoginPasswordDoubleCheck())) {
            bindingResult.rejectValue("loginPasswordDoubleCheck", "Incorrect");
        }
        if(!serviceJoin.idDuplicateCheck(memberJoinDto.getLoginId())) {
            bindingResult.rejectValue("loginId", "Duplicated");
        }

        // 검증 오류
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "home/join";
        }

        // 회원가입 성공
        serviceJoin.join(memberJoinDto);

        // 자동 로그인
        Member loginMember = serviceLogin.login(memberJoinDto.getLoginId(), memberJoinDto.getLoginPassword());
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:/";
    }



    @PostConstruct
    public void init() {
        MemberJoinDto member1 = new MemberJoinDto("sitsit8800", "Arkskekfk1!", "Arkskekfk1!",
                "SIT", "wjsdj2008@gmail.com", "01087144246", true);
        MemberJoinDto member2 = new MemberJoinDto("gmwire4485", "Arkskekfk1!", "Arkskekfk1!",
                "광명와이어", "wjsdj2008@gmail.com", "01087144246", true);
        serviceJoin.join(member1);
        serviceJoin.join(member2);
    }

    private MemberJoinDto initMemberJoinForm() {
        return new MemberJoinDto("wjsdj2009", "Arkskekfk1!", "Arkskekfk1!",
                "제이피컴퍼니", "wjsdj2008@gmail.com", "01087144246", true);
    }

}
