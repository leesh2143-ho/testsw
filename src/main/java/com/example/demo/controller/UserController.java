package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        return "users/user-form";
    }

    @PostMapping("/new")
    public String create(User user) {
        userService.join(user);
        return "redirect:/users";
    }

    @GetMapping
    public String list(@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<User> users = userService.findUsers(pageable);
        model.addAttribute("users", users);
        return "users/user-list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        User user = userService.findOne(id);
        model.addAttribute("user", user);
        return "users/user-details";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findOne(id);
        model.addAttribute("user", user);
        return "users/user-form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/users/{id}";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}