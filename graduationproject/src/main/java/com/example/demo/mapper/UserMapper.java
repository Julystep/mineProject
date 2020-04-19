package com.example.demo.mapper;

import com.example.demo.bean.Role;
import com.example.demo.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    /*登录时的用户名称*/
    User loadUserByUsername(String username);

    /*获取用户权限： 一对多*/
    List<Role> getRolesByUserId(int id);

}
