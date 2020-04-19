package com.example.demo.mapper.root;

import com.example.demo.bean.Academy;
import com.example.demo.bean.Admin;
import com.example.demo.bean.Major;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface RootMapper {

    List<Admin> getSecondaryAdministrator(int page, int size, String userInfo);

    List<Academy> getAllMajor();

    boolean insertAdmin(@Param("admin") Admin admin);

    boolean insertAdmin_info(@Param("admin") Admin admin, @Param("majorList") List<Integer> majorList);

    boolean deleteAdmin(String user_id);

    boolean deleteMajor(String user_id);

    boolean insertRole(Admin admin);

    List<String> getAllID();

    boolean createAcamedy(@Param("list") List<String> list);

    List<String> getAllAcademy();

    List<Academy> getAllAcademy1();

    boolean deleteAcademy(int academyID);

    boolean updateAcademyname(int academy_id, String academy_name);

    boolean createMajor(int academy_id, @Param("set") Set set);

    boolean deleteMajor1(int majorID);

    boolean updateMajorname(int major_id, String major_name);
}
