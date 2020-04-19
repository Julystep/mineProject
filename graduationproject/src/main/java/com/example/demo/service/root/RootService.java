package com.example.demo.service.root;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bean.Academy;
import com.example.demo.bean.Admin;
import com.example.demo.bean.Major;
import com.example.demo.mapper.root.RootMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RootService {

    @Resource
    RootMapper rootMapper;

    public List<Admin> getSecondaryAdministrator(int page, int size, String userInfo) {

        page = (page - 1) * 10;
        return rootMapper.getSecondaryAdministrator(page, size, userInfo);

    }

    public List<Academy> getAllMajor() {

        return rootMapper.getAllMajor();

    }

    public boolean insertAdmin(String form) {

        List<String> ids = rootMapper.getAllID();

        JSONObject formData = JSON.parseObject(form);
        /*获取id*/
        String user_id = formData.getString("user_id");
        if(ids.contains(user_id)){
            return false;
        }
        /*获取用户名称*/
        String username = formData.getString("username");
        /*获取用户邮箱*/
        String email = formData.getString("email");
        /*获取用户电话*/
        String phone = formData.getString("phone");
        /*获取专业管理员参与管理的专业*/
        List<Integer> majorList = new ArrayList<Integer>();
        JSONArray major_id = formData.getJSONArray("major_id");
        for(int i = 0; i < major_id.size(); i++){
            majorList.add(major_id.getJSONArray(i).getInteger(1));
        }

        Admin admin = new Admin();
        admin.setUser_id(user_id);
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPhone(phone);

        return rootMapper.insertAdmin(admin) && rootMapper.insertRole(admin) && rootMapper.insertAdmin_info(admin, majorList);

    }

    public boolean deleteAdmin(String user_id) {

        return rootMapper.deleteAdmin(user_id);

    }

    public boolean changAdminMajor(String user_id, String form) {

        JSONObject formData = JSON.parseObject(form);
        List<Integer> majorList = new ArrayList<Integer>();
        JSONArray major_id = formData.getJSONArray("major_id");
        for(int i = 0; i < major_id.size(); i++){
            System.out.println(major_id.get(i));
            majorList.add(major_id.getJSONArray(i).getInteger(1));
        }
        Admin admin = new Admin();
        admin.setUser_id(user_id);
        boolean flag = rootMapper.deleteMajor(user_id);
        if(flag){
            System.out.println("==============================删除成功，进行添加=============================");
            return rootMapper.insertAdmin_info(admin, majorList);
        }else{
            System.out.println("==============================删除失败，进行添加=============================");
            return false;
        }

    }

    public boolean createAcamedy(String form) {

        List<String> acamedyList = rootMapper.getAllAcademy();

        JSONObject jsonObject = JSON.parseObject(form);

        JSONArray jsonArray = jsonObject.getJSONArray("academy");

        List<String> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();

        for(int i = 0; i < jsonArray.size(); i++) {
            if (!acamedyList.contains(JSON.parseObject(jsonArray.getString(i)).getString("value"))) {
                list.add(JSON.parseObject(jsonArray.getString(i)).getString("value"));
                acamedyList.add(JSON.parseObject(jsonArray.getString(i)).getString("value"));
            }
        }

        return rootMapper.createAcamedy(list);


    }

    public List<Academy> getAllAcademy() {

        return rootMapper.getAllAcademy1();

    }

    public boolean deleteAcademy(int academyID) {

        return rootMapper.deleteAcademy(academyID);

    }

    public boolean updateAcademyname(String form) {

        JSONObject jsonObject = JSON.parseObject(form);
        int academy_id = jsonObject.getInteger("academy_id");
        String academy_name = jsonObject.getString("academy_name");

        return rootMapper.updateAcademyname(academy_id, academy_name);

    }

    public boolean createMajor(String form) {

        JSONObject jsonObject = JSON.parseObject(form);

        int academy_id = jsonObject.getInteger("academy_id");

        JSONArray jsonArray = jsonObject.getJSONArray("label");

        Set<String> set = new HashSet<>();

        for(int i = 0; i < jsonArray.size(); i++){
            set.add(jsonArray.getJSONObject(i).getString("value"));
        }

        return rootMapper.createMajor(academy_id, set);

    }

    public boolean deleteMajor(int majorID) {

        return rootMapper.deleteMajor1(majorID);

    }

    public boolean updateMajorname(String form) {

        JSONObject jsonObject = JSON.parseObject(form);

        int major_id = jsonObject.getInteger("value");

        String major_name = jsonObject.getString("label");

        return rootMapper.updateMajorname(major_id, major_name);

    }
}
