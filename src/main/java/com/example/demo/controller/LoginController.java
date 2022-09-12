package com.example.demo.controller;

import com.example.demo.models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginInPage(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            return "redirect:/post/all";
        }
        user = new User();
        model.addAttribute("user", user);
        return "login";
    }
}