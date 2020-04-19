package com.example.demo.controller;

import com.example.demo.bean.RespBean;
import com.example.demo.common.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private SimpMessagingTemplate template;

    @RequestMapping("/sessionouttime")
    public RespBean sessionOutTime(){

        RespBean respBean = RespBean.warn("用户登录已过期，请重新登录");

        return respBean;


    }

}
