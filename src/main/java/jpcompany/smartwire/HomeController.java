package jpcompany.smartwire;

import jpcompany.smartwire.domain.member.Member;
import jpcompany.smartwire.domain.member.MemberLoginForm;
import jpcompany.smartwire.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final MemberRepository repository;

//    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("memberLoginForm", new MemberLoginForm("sitsit8800"));
        return "home/login";
    }
}

