package com.example.demo.mapper.mail;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MailMapper {


    public String getEmailByUser_id(String user_id);
}
