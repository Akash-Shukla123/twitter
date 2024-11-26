package org.nov2024.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class HomeController {

    @GetMapping("/")
    public RedirectView home(){
        return new RedirectView("/index.html");
    }

    @GetMapping("/role")
    public RedirectView role(){
        return new RedirectView("/role.html");
    }
}
