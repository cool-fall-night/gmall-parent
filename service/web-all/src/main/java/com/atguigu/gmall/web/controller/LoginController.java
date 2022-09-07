package com.atguigu.gmall.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 毛伟臣
 * 2022/9/7
 * 13:36
 * @version 1.0
 * @since JDK1.8
 */

@Controller
public class LoginController {

//    http://passport.gmall.com/login.html?originUrl=http://gmall.com/?
    @GetMapping("/login.html")
    public String loginPage(@RequestParam("originUrl") String originUrl, Model model){
        model.addAttribute("originUrl",originUrl);
        return "login";
    }


}
