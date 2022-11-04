package com.vyas.user_management.Controller;

import com.vyas.user_management.Entity.User;
import com.vyas.user_management.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ModelAttribute
    public void userDetails(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        model.addAttribute("user", user);
    }

    @GetMapping("/")
    public String home() {
        return "User/home";
    }

    @GetMapping("/changePassword")
    public String chanePassword() {
        return "User/change_password";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(Principal principal, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, HttpSession session) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        boolean u = bCryptPasswordEncoder.matches(oldPassword, user.getPassword());

        if (u) {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            User updateUserPassword = userRepository.save(user);
            if (updateUserPassword != null) {
                session.setAttribute("msg", "Password Updated");
            } else {
                session.setAttribute("msg", "Something went wrong");
            }
        } else {
            session.setAttribute("msg","Old Password is incorrect");
        }
        return "User/change_password";
    }
}
