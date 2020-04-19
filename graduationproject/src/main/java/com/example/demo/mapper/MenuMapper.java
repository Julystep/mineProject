package com.example.demo.mapper;

import com.example.demo.bean.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {

    /*获得所有的路由*/
    List<Menu> getAllMenu();

    /*通过id获取路由信息*/
    List<Menu> getMenusByUserId(String id);

}
