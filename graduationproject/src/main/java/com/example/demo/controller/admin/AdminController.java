package com.example.demo.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bean.*;
import com.example.demo.common.PoiUtils;
import com.example.demo.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @RequestMapping(value = "/getmajorid", method = RequestMethod.GET)
    public Admin getMajorID(@RequestParam("userID") String userID){

        return adminService.getMajorID(userID);

    }

    @RequestMapping(value = "/getstudentbymajor", method = RequestMethod.POST)
    public Map<String, Object> getStudentByMajor(@RequestParam("majorList") String majorList,
                                                 @RequestParam("currentPage") int page,
                                                 @RequestParam("size") int size,
                                                 @RequestParam("form") String form){

        return adminService.getStudentByMajor(majorList, page, size, form);

    }

    @RequestMapping(value = "/getteacherbymajor", method = RequestMethod.POST)
    public Map<String, Object> getTeacherByMajor(@RequestParam("majorList") String majorList,
                                                 @RequestParam("currentPage") int page,
                                                 @RequestParam("size") int size,
                                                 @RequestParam("form") String form){

        return adminService.getTeacherByMajor(majorList, page, size, form);

    }

    @RequestMapping(value = "/getclassesbymajorIid", method = RequestMethod.GET)
    public List<Major> getClassesByMajorId(@RequestParam("majorID") int majorID){

        return adminService.getClassesByMajorId(majorID);

    }

    @RequestMapping(value = "/insertstudentinfo", method = RequestMethod.POST)
    public RespBean insertStudentInfoDetail(@RequestParam("form") String form){

        if(adminService.insertStudentInfoDetail(form)){
            return RespBean.ok("插入学生信息成功");
        }else{
            return RespBean.error("插入学生信息失败");
        }

    }

    @RequestMapping(value = "/changestudentinfo", method = RequestMethod.POST)
    public RespBean changeStudentInfoDetail(@RequestParam("form") String form){

        if(adminService.changeStudentInfoDetail(form)){
            return RespBean.ok("修改学生信息成功");
        }else{
            return RespBean.error("修改学生信息失败");
        }

    }

    @RequestMapping(value = "/deleteStudentInfo", method = RequestMethod.POST)
    public RespBean deleteStudentInfoDetail(@RequestParam("user_id") String userID){

        if(adminService.deleteStudentInfoDetail(userID)){
            return RespBean.ok("删除学生信息成功");
        }else{
            return RespBean.error("删除学生信息失败");
        }

    }

    @RequestMapping(value = "/insertteacherinfo", method = RequestMethod.POST)
    public RespBean insertTeacherInfoDetail(@RequestParam("form") String form){

        if(adminService.insertTeacherInfoDetail(form)){
            return RespBean.ok("插入教师信息成功");
        }else{
            return RespBean.error("插入教师信息失败");
        }

    }

    @RequestMapping(value = "/changeteacherinfo", method = RequestMethod.POST)
    public RespBean changeTeacherInfoDetail(@RequestParam("form") String form){

        if(adminService.changeTeacherInfoDetail(form)){
            return RespBean.ok("修改教师信息成功");
        }else{
            return RespBean.error("修改教师信息失败");
        }

    }

    @RequestMapping(value = "/deleteTeacherInfo", method = RequestMethod.POST)
    public RespBean deleteTeacherInfoDetail(@RequestParam("user_id") String userID){

        if(adminService.deleteTeacherInfoDetail(userID)){
            return RespBean.ok("删除教师信息成功");
        }else{
            return RespBean.error("删除教师信息失败");
        }

    }

    @RequestMapping(value = "/getallgrade", method = RequestMethod.POST)
    public List<Major> getAllMajor(@RequestParam("majorList") String majorList){

        return adminService.getAllGrade(majorList);

    }

    @RequestMapping(value = "/updategrade", method = RequestMethod.POST)
    public RespBean updateGrade(@RequestParam("form") String form){

        if(adminService.updateGrade(form)){
            return RespBean.ok("更新班级名称成功");
        }else{
            return RespBean.error("更新班级名称失败");
        }

    }

    @RequestMapping(value = "/updateclass", method = RequestMethod.POST)
    public RespBean updateClass(@RequestParam("form") String form){

        if(adminService.updateClass(form)){
            return RespBean.ok("更新班级名称成功");
        }else{
            return RespBean.error("更新班级名称失败");
        }

    }

    @RequestMapping(value = "/deletegrade", method = RequestMethod.POST)
    public RespBean deleteGrade(@RequestParam("grade_id") int grade_id){

        boolean flag = adminService.deleteGrade(grade_id);

        if(flag){
            return RespBean.ok("删除年级成功");
        }else{
            return RespBean.error("删除年级失败");
        }

    }

    @RequestMapping(value = "/deleteclass", method = RequestMethod.POST)
    public RespBean deleteClass(@RequestParam("class_id") int class_id){

        boolean flag = adminService.deleteClass(class_id);

        if(flag){
            return RespBean.ok("删除班级成功");
        }else{
            return RespBean.error("删除班级失败");
        }

    }

    @RequestMapping(value = "/insertgrade", method = RequestMethod.POST)
    public RespBean insertGrade(@RequestParam("form") String form){

        boolean flag = adminService.insertGrade(form);

        if(flag){
            return RespBean.ok("添加年级成功");
        }else{
            return RespBean.error("添加年级失败");
        }

    }

    @RequestMapping(value = "/insertclass", method = RequestMethod.POST)
    public RespBean insertClass(@RequestParam("form") String form){

        boolean flag = adminService.insertClass(form);

        if(flag){
            return RespBean.ok("添加班级成功");
        }else{
            return RespBean.error("添加班级失败");
        }

    }

    @RequestMapping(value = "/getmajorandgrade", method = RequestMethod.POST)
    public List<Major> getMajorAndGrade(@RequestParam("majorList") String majorList){
        return adminService.getMajorAndGrade(majorList);
    }

    @RequestMapping(value = "/importstudents", method = RequestMethod.POST)
    public RespBean importStudents(MultipartFile file){
        List<Student> students = PoiUtils.importStudents(file);

        boolean flag = adminService.importStudents(students);

        if(flag){
            return RespBean.ok("导入学生成功");
        }else{
            return RespBean.error("导入学生失败, 可能原因为数据库不存在该学生填写的班级或学号不和规范");
        }
    }

    @RequestMapping(value = "/importteachers", method = RequestMethod.POST)
    public RespBean importTeachers(MultipartFile file){
        List<Teacher> teachers = PoiUtils.importTeachers(file);

        boolean flag = adminService.importTeachers(teachers);

        if(flag){
            return RespBean.ok("导入教师成功");
        }else{
            return RespBean.error("导入教师失败, 可能原因为数据库不存在该学生填写的班级或学号不和规范");
        }
    }

    @RequestMapping(value = "/getallcourse", method = RequestMethod.POST)
    public List<Major> getAllCourse(@RequestParam("majorList") String majorList){
        return adminService.getallCourse(majorList);
    }

    @RequestMapping(value = "/updatecourse", method = RequestMethod.POST)
    public RespBean updateCourse(@RequestParam("form") String form){

        if(adminService.updateCourse(form)){
            return RespBean.ok("更新课程名称成功");
        }else{
            return RespBean.error("更新课程名称失败");
        }

    }

    @RequestMapping(value = "/deletecourse", method = RequestMethod.POST)
    public RespBean deleteCourse(@RequestParam("course_id") int course_id){

        boolean flag = adminService.deleteCourse(course_id);

        if(flag){
            return RespBean.ok("删除课程成功");
        }else{
            return RespBean.error("删除课程失败");
        }

    }

    @RequestMapping(value = "/insertcourse", method = RequestMethod.POST)
    public RespBean insertCourse(@RequestParam("form") String form){

        boolean flag = adminService.insertCourse(form);

        if(flag){
            return RespBean.ok("添加课程成功");
        }else{
            return RespBean.error("添加课程失败");
        }

    }








}
