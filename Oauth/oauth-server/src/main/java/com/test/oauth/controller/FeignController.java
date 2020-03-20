package com.test.oauth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 李刚刚
 * @date: 2020-03-17 15:01
 * @description:
 * @history:
 */
@RestController
@RequestMapping("/test")
public class FeignController {

    @RequestMapping(value = "/msg",method = RequestMethod.GET)
    public String getRemoteMsg() {
        return "success";
    }
}
