package com.test.client.feign.impl;

import com.test.client.feign.UserFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author: 李刚刚
 * @date: 2020-03-17 14:14
 * @description:
 * @history:
 */
@Component
public class UserFeignClientFallBack implements UserFeignClient {
    @Override
    public String getRemoteMsg() {
        return null;
    }
}
