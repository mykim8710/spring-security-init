package spring.security.init.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.security.init.dto.ResponseUserSignInDto;

@Slf4j
@Controller
public class ViewController {
    @GetMapping("/")
    public String indexView(//@RequestParam(value = "error", required = false) String error,
                            //@RequestParam(value = "exception", required = false) String exception,
                            //Model model
                            ) {
        log.info("[GET] /  => root(sign in) page view");

        // Form login 방식
//        model.addAttribute("error", error);
//        model.addAttribute("exception", exception);

        return "index";
    }

    @GetMapping("/home")
    public String homeView(@AuthenticationPrincipal ResponseUserSignInDto dto) {
        log.info("[GET] /home  => home page view");
        log.info("ResponseUserSignInDto : {}", dto);
        return "home";
    }

    @GetMapping("/admin")
    public String adminView(@AuthenticationPrincipal ResponseUserSignInDto dto) {
        log.info("[GET] /admin  => ADMIN Role page view");
        log.info("ResponseUserSignInDto : {}", dto);
        return "authorization/admin";
    }

    @GetMapping("/user")
    public String userView(@AuthenticationPrincipal ResponseUserSignInDto dto) {
        log.info("[GET] /admin  => USER Role page view");
        log.info("ResponseUserSignInDto : {}", dto);
        return "authorization/user";
    }


    @Secured("ROLE_ADMIN") // 클래스, 메서드 별 개별로 권한처리 시 사용하는 어노테이션(함수 샐행 전, 권한 하나만)
    @GetMapping("/info")
    @ResponseBody
    public String securedTest(@AuthenticationPrincipal ResponseUserSignInDto dto) {
        log.info("[GET] /info");
        log.info("ResponseUserSignInDto : {}", dto);
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')") // 함수 실행 전, 여러개 권한 체크
    @GetMapping("/info2")
    @ResponseBody
    public String securedTest2(@AuthenticationPrincipal ResponseUserSignInDto dto) {
        log.info("[GET] /info2");
        log.info("ResponseUserSignInDto : {}", dto);
        return "개인정보2";
    }

    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')") // 함수 실행 후, 여러개 권한 체크
    @GetMapping("/info3")
    @ResponseBody
    public String securedTest3(@AuthenticationPrincipal ResponseUserSignInDto dto) {
        log.info("[GET] /info3");
        log.info("ResponseUserSignInDto : {}", dto);
        return "개인정보3";
    }

}
