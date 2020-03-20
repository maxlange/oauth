package com.test.client.feign;

import com.test.client.feign.impl.UserFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: 李刚刚
 * @date: 2020-03-17 14:12
 * @description:
 * @history:
 */
@FeignClient(value = "oauth-server",fallback = UserFeignClientFallBack.class)
public interface UserFeignClient {

    @RequestMapping(value = "/test/msg",method = RequestMethod.GET)
    public String getRemoteMsg();
}
