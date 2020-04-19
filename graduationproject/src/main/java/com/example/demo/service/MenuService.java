package com.example.demo.service;

import com.example.demo.bean.Menu;
import com.example.demo.common.UserUtils;
import com.example.demo.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    MenuMapper menuMapper;

    public List<Menu> getAllMenu(){
        return menuMapper.getAllMenu();
    }

    public List<Menu> getMenusByUserId() { return menuMapper.getMenusByUserId(UserUtils.getCurrentUser().getUser_id()); }

}
