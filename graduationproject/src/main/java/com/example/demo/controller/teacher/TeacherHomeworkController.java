package com.example.demo.controller.teacher;

import com.example.demo.bean.*;
import com.example.demo.service.teacher.TeacherHomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/teacher")
public class TeacherHomeworkController {

    @Autowired
    TeacherHomeworkService teacherHomeworkService;

    /*得到教师教授学科*/
    @RequestMapping(value="/getsubjectsbyteacherid",method = RequestMethod.GET)
    public Map<String, Object> getSubjectsByTeacherId(String teacherID){
        Map<String, Object> map = new HashMap<>();
        List<Course> courseList = teacherHomeworkService.getSubjectsByTeacherId(teacherID);
        map.put("subject", courseList);
        return map;
    }

    /*通过课程id获得学习这个课程的班级*/
    @RequestMapping(value = "/getclassesbycourseid2", method = RequestMethod.GET)
    public Map<String, Object> getClassesByCourseId2(String courseID){
        Map<String, Object> map = new HashMap<>();
        List<Major> cgmaBeanList = teacherHomeworkService.getClassesByCourseId2(courseID);
        map.put("list", cgmaBeanList);
        return map;
    }

    /*教师发布作业内容*/
    @RequestMapping(value = "/subhomework", method = RequestMethod.POST)
    public RespBean onSubmitHomework(@RequestParam("form") String form){


        boolean flag = teacherHomeworkService.onSubmitHomework(form);

        if(flag){
            return RespBean.ok("作业发布成功");
        }else {
            return RespBean.error("作业发布失败");
        }

    }




}
