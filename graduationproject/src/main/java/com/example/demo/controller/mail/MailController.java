package com.example.demo.controller.mail;

import com.example.demo.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    @Autowired
    MailService mailService;

    @RequestMapping(value = "/mail/getveritycode", method = RequestMethod.GET)
    public String getVerityCode(@RequestParam("receiver") String receiver){

        try{
            String verCode = mailService.sendEmailVerCode(receiver);
            return verCode;
        }catch (Exception e){
            return "发送失败";
        }

    }

    @RequestMapping(value = "/getemailbyuser_id", method = RequestMethod.GET)
    public String getEmailByUser_id(@RequestParam("user_id") String user_id){

        String email =  mailService.getEmailByUser_id(user_id);
        return email;

    }

}
