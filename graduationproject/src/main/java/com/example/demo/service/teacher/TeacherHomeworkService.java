package com.example.demo.service.teacher;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bean.*;
import com.example.demo.mapper.teacher.TeacherHomeworkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TeacherHomeworkService {

    @Resource
    TeacherHomeworkMapper teacherHomeworkMapper;



    /*获得教师所教授课程*/
    public List<Course> getSubjectsByTeacherId(String teacherID) {
        return teacherHomeworkMapper.getSubjectsByTeacherId(teacherID);
    }
    /*获得学习这门专业的班级2*/
    public List<Major> getClassesByCourseId2(String courseID){
        int courseID1 = Integer.parseInt(courseID);
        return teacherHomeworkMapper.getClassesByCourseId2(courseID1);
    }
    /*教师发布作业内容*/
    public boolean onSubmitHomework(String form){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JSONObject jsonObject = JSON.parseObject(form);
        /*获取教师id*/
        String teacherID = (String) jsonObject.get("teacherID");
        /*获取作业题目*/
        String title = jsonObject.getString("title");
        /*获取作业内容*/
        String main =  jsonObject.getString("main");
        /*获取开始日期和截止日期*/
        JSONArray jsonArray = jsonObject.getJSONArray("dateValue");
        Date sdate = jsonArray.getDate(0);
        Date edate = jsonArray.getDate(1);
        /*获取作业的评判标准*/
        String judge = jsonObject.getString("judgeRules");
        Homework homework = new Homework();
        homework.setTitle(title);
        homework.setMain(main);
        homework.setSdate(sdate);
        homework.setEdate(edate);
        homework.setJudge(judge);
        /*获取需要完成该作业的班级*/
        JSONArray jsonArray1 = jsonObject.getJSONArray("classValue");
        List<Integer> classIDList = new ArrayList<>();
        JSONArray jsonArray2 = null;
        for(int i = 0; i < jsonArray1.size(); i++){
            jsonArray2  = jsonArray1.getJSONArray(i);
            classIDList.add(jsonArray2.getInteger(Integer.parseInt("2")));
        }
        //获取学科id
        int courseID = jsonObject.getInteger("courseID");
        //获取id，并且id存储于homework.getId()中
        if(teacherHomeworkMapper.onSubmitHomework(homework) ) {
           // 将作业和老师和课程绑定
           // 将作业和班级绑定
            //将作业以状态形式和学生绑定
            if(teacherHomeworkMapper.setHomeworkWithClass(classIDList, homework.getId())){
                List<String> studentID = teacherHomeworkMapper.getSudentIdsByClasses(homework.getId());
                if(studentID.size() == 0){
                    return false;
                }
                if(teacherHomeworkMapper.setStudentInfoInHomeworkClass(studentID, homework.getId()) &&
                        teacherHomeworkMapper.setHomeworkWithTeacherAndCourse(teacherID, courseID, homework.getId())){

                    return true;

                }
            }
        }
        return false;
    }

}
