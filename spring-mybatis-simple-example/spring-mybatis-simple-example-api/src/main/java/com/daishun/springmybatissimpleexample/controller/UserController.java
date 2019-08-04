package com.daishun.springmybatissimpleexample.controller;

import com.daishun.springmybatissimpleexample.repository.domain.User;
import com.daishun.springmybatissimpleexample.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author daishun
 * @since 2019/7/30
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "getAllUsers")
    public List<User> getAllUsers() {
        return userService.getUserList();
    }

    @GetMapping(value = "getUserById")
    public User getUserById(@RequestParam("id") Long id) {
        return userService.getUserById(id);
    }
}
