package jpcompany.smartwire.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberMyPageController {

    @GetMapping("/my_page")
    public String myPage(@RequestParam String companyName, Model model) {
        model.addAttribute("companyName", companyName);
        return "home/my_page";
    }
}
