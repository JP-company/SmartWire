package jpcompany.smartwire.web.member.controller;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.web.member.dto.MemberJoinDto;
import jpcompany.smartwire.web.member.service.MemberJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberJoinController {

    private final MemberJoinService serviceJoin;

    @GetMapping("/join")
    public String join(Model model) {
//        model.addAttribute("memberJoinForm", new MemberJoinForm());
        model.addAttribute("memberJoinDto", initMemberJoinForm());
        return "home/join";
    }

    @PostMapping("/join")
    public String joinIn(@Validated @ModelAttribute(name = "memberJoinDto") MemberJoinDto memberJoinDto,
                         BindingResult bindingResult,
                         Model model)
            throws MessagingException, UnsupportedEncodingException {

        // 비밀번호 확인 불일치, 아이디 중복 오류
        if (!serviceJoin.passwordDoubleCheck(memberJoinDto.getLoginPassword(), memberJoinDto.getLoginPasswordDoubleCheck())) {
            bindingResult.rejectValue("loginPasswordDoubleCheck", "Incorrect");
        }
        if (!serviceJoin.idDuplicateCheck(memberJoinDto.getLoginId())) {
            bindingResult.rejectValue("loginId", "Duplicated");
        }

        // 검증 오류 시
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "home/join";
        }

        // 회원가입 성공 -> 메일 인증 요청 페이지 이동
        serviceJoin.join(memberJoinDto);
        model.addAttribute("member", memberJoinDto);
        return "home/email_verify";
    }

    private MemberJoinDto initMemberJoinForm() {
        return new MemberJoinDto("wjsdj2009", "Arkskekfk1!", "Arkskekfk1!",
                "제이피컴퍼니", "wjsdj2008@gmail.com", "01087144246", true);
    }
}
