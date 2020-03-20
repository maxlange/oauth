package com.test.resource.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author: 李刚刚
 * @date: 2020-03-18 16:22
 * @description:
 * @history:
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @GetMapping("/test")
    public String getResource(){
        return "it is resource";
    }

    @PreAuthorize("ADMIN")
    @GetMapping("/user")
    public Principal getUser(Principal user) {
        return user;
    }
}
