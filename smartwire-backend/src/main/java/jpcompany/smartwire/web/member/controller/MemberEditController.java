package jpcompany.smartwire.web.member.controller;

import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.security.common.PrincipalDetails;
import jpcompany.smartwire.web.member.dto.MemberUpdateDto;
import jpcompany.smartwire.web.member.service.MemberEditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberEditController {

    private final MemberEditService memberEditService;

    @GetMapping("/member")
    public String member(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Member member = principalDetails.getMember();
        model.addAttribute("member", member);
        return "home/member";
    }

    @PostMapping("/member")
    public String postMember(@ModelAttribute(name = "memberUpdateDto") MemberUpdateDto memberUpdateDto,
                             HttpServletRequest request, RedirectAttributes redirectAttrs) {
        Member member = memberEditService.updateCompanyNameNPhoneNumber(memberUpdateDto);

        // 세션 업데이트
        PrincipalDetails newPrincipalDetails = new PrincipalDetails(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                newPrincipalDetails,    // 변경된 정보를 가진 새로운 PrincipalDetails
                newPrincipalDetails.getPassword(),    // 보통 기존 패스워드를 그대로 사용
                newPrincipalDetails.getAuthorities());    // 보통 기존 권한을 그대로 사용
        SecurityContextHolder.getContext().setAuthentication(authentication);

        redirectAttrs.addFlashAttribute("popupMessage", "변경 사항이 저장되었습니다.");
        return "redirect:/member";
    }
}
