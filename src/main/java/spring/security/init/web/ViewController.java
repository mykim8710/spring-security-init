package spring.security.init.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import spring.security.init.dto.ResponseUserSignInDto;

import java.security.Principal;

@Slf4j
@Controller
public class ViewController {

    @GetMapping("/")
    public String indexView() {
        return "index";
    }






    @GetMapping("/home")
    public String homeView(@AuthenticationPrincipal ResponseUserSignInDto dto) {
        System.out.println("principal = " + dto);
        return "home";
    }

    @GetMapping("/admin")
    public String adminView(@AuthenticationPrincipal ResponseUserSignInDto dto) {
        System.out.println("principal = " + dto);
        return "authorization/admin";
    }

    @GetMapping("/user")
    public String userView(@AuthenticationPrincipal ResponseUserSignInDto dto) {
        System.out.println("principal = " + dto);
        return "authorization/user";
    }


}
