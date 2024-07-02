package com.codegym.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {
    private String getPrincipal() {
        /*TODO: Principal: là lớp mặc định của Spring Security
           để lưu thông tin người dùng đăng nhập*/
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        /*TODO: SecurityContextHolder: cung cấp ngữ cảnh truy cập
           bảo mật để có thể truy xuất các thông tin bảo mật được phép,
            trong đó có thông tin người dùng.*/

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }

        return userName;
    }


    @GetMapping(value = {"/", "/home"})
    public String Homepage(Model model) {
        model.addAttribute("user", getPrincipal());
        return "/welcome";
    }

    @GetMapping("/admin")
    public String adminPage(ModelMap modelMap) {
        modelMap.addAttribute("user", getPrincipal());
        return "/admin";
    }

    /*TODO: Bước 8: Xây dựng view và controller cho role DBA*/
    @GetMapping("/dba")
    public String dbaPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "/dba";
    }

    /*TODO: Bước 7: Xây dựng view và controller xử lý khi truy cập không được phép*/
    @GetMapping("/accessDenied")
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "/access_denied";
    }
}
