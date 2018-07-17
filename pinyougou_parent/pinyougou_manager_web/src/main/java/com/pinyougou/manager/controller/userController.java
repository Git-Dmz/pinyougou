package com.pinyougou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class userController {

    @RequestMapping("findUserName")
    public String findUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}