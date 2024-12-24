package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping(value = "/admin/showList")
    public String showList() {
        return "result";
    }

    @GetMapping(value = "/user")
    public String user() {
        return "user";
    }

}
