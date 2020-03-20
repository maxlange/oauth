package com.test.client.controller;

import com.test.client.feign.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 李刚刚
 * @date: 2020-03-17 14:16
 * @description:
 * @history:
 */
@RestController
@RequestMapping(value = "/client")
public class ClientController {

    @Autowired
    private UserFeignClient userFeignClient;

    @GetMapping("/msg")
    public String getMsg(){
        String remoteMsg = userFeignClient.getRemoteMsg();
        return remoteMsg;
    }

    @GetMapping("/test")
    public String getSuccessMsg(){
        return "success";
    }
}
