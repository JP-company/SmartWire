package jpcompany.smartwire.controller.member;

import jpcompany.smartwire.domain.Machine;
import jpcompany.smartwire.domain.Member;
import jpcompany.smartwire.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
        // System.out.println("memberService = " + memberService.getClass());
    }


    @GetMapping("/signin")
    public String createForm() {
        return "member/createMemberForm";
    }
    @PostMapping("/signin")
    public String create(Member memberForm) {
        Member member = new Member();
        member.setLoginId(memberForm.getLoginId());
        member.setLoginPassword(memberForm.getLoginPassword());
        member.setCompanyName(memberForm.getCompanyName());
        member.setMachinesNum(memberForm.getMachinesNum());
        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/account")
    public String account() {
        return "member/input";
    }
    @PostMapping("/account")
    public String getAccount(Member memberForm, RedirectAttributes redirectAttributes) {
        Optional<Member> member = memberService.findOne(memberForm.getLoginId());

        if (member.isEmpty() || !member.get().getLoginPassword().equals(memberForm.getLoginPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "아이디 혹은 비밀번호가 일치하지 않습니다.");
            return "redirect:/account";
        }
        return "redirect:/account/info?id=" + member.get().getId();
    }

    @GetMapping("/account/info")
    public String accountInfo(@RequestParam("id") Long id, Model model) {
        Member member = memberService.findOne(id).get();
        model.addAttribute("member", member);
        model.addAttribute("id", id);
        return "member/info";
    }
    @PostMapping("/account/info")
    public String editAccountInfo(@RequestParam("id") Long id, Member memberForm) {
        Member member = memberService.findOne(id).get();
        member.setLoginPassword(memberForm.getLoginPassword());
        member.setCompanyName(memberForm.getCompanyName());
        member.setMachinesNum(memberForm.getMachinesNum());
        memberService.updateInfo(member);
        return "redirect:/account/info?id=" + id;
    }
}
