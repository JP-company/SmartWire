package jpcompany.smartwire.web.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberProfileController {

    @GetMapping("/profile")
    public String profile() {
        return "/home/profile";
    }

    @PostMapping("/profile")
    public String PostProfile() {
        return "/home/profile";
    }
}
