package com.example.demo.common;

import com.example.demo.bean.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {
    public static User getCurrentUser() {
        /*在springsecurity中获得用户的基本信息*/
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
