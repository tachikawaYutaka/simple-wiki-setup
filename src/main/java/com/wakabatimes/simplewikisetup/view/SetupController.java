package com.wakabatimes.simplewikisetup.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SetupController {
    @GetMapping("/")
    public String setup(){
        return "home";
    }
}
