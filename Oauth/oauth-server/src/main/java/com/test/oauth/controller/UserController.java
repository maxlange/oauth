package com.test.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author: 李刚刚
 * @date: 2020-03-16 19:51
 * @description:
 * @history:
 */
@RestController
public class UserController {

    @GetMapping("/user")
    public Principal getUser(Principal user) {
        return user;
    }
}
