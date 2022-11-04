package com.vyas.user_management.Controller;

import com.vyas.user_management.Entity.User;
import com.vyas.user_management.Repository.UserRepository;
import com.vyas.user_management.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ModelAttribute
    public void userDetails(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            model.addAttribute("user", user);
        }
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute User user, HttpSession session) {
        boolean u = userService.checkEmail(user.getEmail());
        if (u) {
            session.setAttribute("msg", "Email is already exist.");
        } else {
            User user1 = userService.createUser(user);
            if (user1 != null) {
                session.setAttribute("msg", "Register Successfully.");
            } else {
                session.setAttribute("msg", "Something went wrong.");
            }
        }
        return "redirect:/register";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "forgot_password";
    }

    @GetMapping("/resetPassword/{id}")
    public String resetPassword(@PathVariable int id, Model model) {
        model.addAttribute("id",id);
        return "reset_password";
    }

    @PostMapping("/processForgotPassword")
    public String processForgotPassword(@RequestParam("email") String email, @RequestParam("number") String number, HttpSession session) {
        User user = userRepository.findByEmailAndNumber(email, number);
        if (user != null) {
            return "redirect:/resetPassword/" + user.getId();
        } else {
            session.setAttribute("msg","Invalid Email or Contact Number");
            return "forgot_password";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("psw") String password, @RequestParam int id, HttpSession session){
        User user = userRepository.findById(id).get();
        String encryptPass = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptPass);
        User save = userRepository.save(user);
        if(save != null) {
            session.setAttribute("msg","Password updated Successfully");
        }
        return "redirect:/signin";
    }
}
