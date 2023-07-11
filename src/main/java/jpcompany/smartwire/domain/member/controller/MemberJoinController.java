package jpcompany.smartwire.domain.member.controller;

import jpcompany.smartwire.domain.member.MemberJoinForm;
import jpcompany.smartwire.domain.member.service.join.MemberServiceJoin;
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

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberJoinController {
    private final MemberServiceJoin serviceJoin;

    @GetMapping("/join")
    public String join(Model model) {
//        model.addAttribute("memberJoinForm", new MemberJoinForm());
        model.addAttribute("memberJoinForm", initMemberJoinForm());
        return "home/join";
    }

    @PostMapping("/join")
    public String joinIn(@Validated @ModelAttribute(name = "memberJoinForm") MemberJoinForm memberJoinForm, BindingResult bindingResult) {

        // 검증 로직
        if(!serviceJoin.passwordDoubleCheck(memberJoinForm.getLoginPassword(), memberJoinForm.getLoginPasswordDoubleCheck())) {
            bindingResult.rejectValue("loginPasswordDoubleCheck", "Incorrect");
        }
        if(!serviceJoin.idDuplicateCheck(memberJoinForm.getLoginId())) {
            bindingResult.rejectValue("loginId", "Duplicated");
        }
        log.info("memberJoinForm={}", memberJoinForm);

        // 검증 오류
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "home/join";
        }

        // 회원가입 성공
        serviceJoin.join(memberJoinForm);
        return "redirect:/";
    }

    @PostConstruct
    public void init() {
        MemberJoinForm member1 = new MemberJoinForm("sitsit8800", "Arkskekfk1!", "Arkskekfk1!",
                "SIT", "wjsdj2008@gmail.com", "01087144246", true);
        MemberJoinForm member2 = new MemberJoinForm("gmwire4485", "Arkskekfk1!", "Arkskekfk1!",
                "광명와이어", "wjsdj2008@gmail.com", "01087144246", true);
        serviceJoin.join(member1);
        serviceJoin.join(member2);
    }

    private MemberJoinForm initMemberJoinForm() {
        return new MemberJoinForm("wjsdj2009", "Arkskekfk1!", "Arkskekfk1!",
                "제이피컴퍼니", "wjsdj2008@gmail.com", "01087144246", true);
    }

}
