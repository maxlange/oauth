package com.test.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: 李刚刚
 * @date: 2020-03-20 13:02
 * @description:
 * @history:
 */
@Controller
@SessionAttributes("{authorizationRequest}")
public class GrantController {

    @GetMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model,
                                              HttpServletRequest request) throws Exception {

//        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model
//                .get("authorizationRequest");
//
//        ModelAndView view = new ModelAndView("base-grant");
//        view.addObject("clientId", authorizationRequest.getClientId());

        return new ModelAndView("base-grant",model);
    }
}