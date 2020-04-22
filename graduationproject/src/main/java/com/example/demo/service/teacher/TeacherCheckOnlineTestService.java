package com.example.demo.service.teacher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bean.ClassStudent;
import com.example.demo.bean.ClassStudentOnlineTest;
import com.example.demo.bean.Examination;
import com.example.demo.mapper.teacher.TeacherCheckOnlineTestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TeacherCheckOnlineTestService {

    @Resource
    TeacherCheckOnlineTestMapper teacherCheckOnlineTestMapper;

    public List<Examination> getOnlineTestInfoByTeacherIdAndCourseId(String teacherID, int courseID) {
        return teacherCheckOnlineTestMapper.getOnlineTestInfoByTeacherIdAndCourseId(teacherID, courseID);
    }

    public Map<String, Object> getOnlineTestDetail(String onlinetest,
                                                   int[] classValue,
                                                   String studentInfo,
                                                   boolean stateChecked,
                                                   boolean commentChecked,
                                                   boolean allChecked,
                                                   int page,
                                                   int size) {
        page = (page - 1) * 20;
        JSONObject jsonObject = JSON.parseObject(onlinetest);
        /*获得作业id*/
        int onlinetestID = jsonObject.getInteger("id");
        /*获得班级*/
        List<Integer> classList = new ArrayList<>();
        if (classValue.length != 0) {
            for (int i = 0; i < classValue.length; i++) {
                classList.add(classValue[i]);
            }
        } else {
            JSONArray jsonArray = jsonObject.getJSONArray("children1");
            JSONObject jsonObject1 = null;
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObject1 = jsonArray.getJSONObject(i);
                classList.add(jsonObject1.getInteger("class_id"));
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("classStudents", teacherCheckOnlineTestMapper.getOnlineTestDetail(onlinetestID, classList, studentInfo, stateChecked, commentChecked, allChecked, page, size));
        map.put("studentsCount", teacherCheckOnlineTestMapper.getOnlineTestCount(onlinetestID, classList, studentInfo, stateChecked, commentChecked, allChecked));
        return map;
    }

    public Map<String, Object> getOnlineTestDetailCount(int onlinetestID, int[] classValue) {
        List<Integer> classList = new ArrayList<>();
        if(classValue.length != 0) {
            for(int i = 0; i < classValue.length; i++){
                classList.add(classValue[i]);
            }
        }
        Map<String, Object> map = new HashMap<>();
        int sumbitCount = teacherCheckOnlineTestMapper.getSubmitCount(onlinetestID, classList);
        int noCheckedCount = teacherCheckOnlineTestMapper.getNoCheckedCount(onlinetestID, classList);
        int noSumbitCount = teacherCheckOnlineTestMapper.getNoSubmitCount(onlinetestID, classList);
        System.out.println(sumbitCount + "   " + noCheckedCount + "   " + noSumbitCount);
        map.put("submitCount", sumbitCount);
        map.put("noCheckedCount", noCheckedCount);
        map.put("noSubmitCount", noSumbitCount);
        return map;
    }

    public boolean changeOnlineTestDate(int onlinetestID, Date[] dateValue) {
        Date sdate = dateValue[0];
        Date edate = dateValue[1];
        return teacherCheckOnlineTestMapper.changeOnlineTestDate(onlinetestID, sdate, edate);
    }

    public boolean deleteOnlineTest(int onlinetestID) {
        return teacherCheckOnlineTestMapper.deleteOnlineTest(onlinetestID);
    }

    public boolean changeStudentexaminationScores(String answer, String studentID, int homeworkID, String commentValue) {
        return teacherCheckOnlineTestMapper.changeStudentexaminationScores(answer, studentID, homeworkID, commentValue);
    }

    public boolean changeExaminationComment(int homeworkID, String studentID, String main) {
        return teacherCheckOnlineTestMapper.changeExaminationComment(homeworkID, studentID, main);
    }

    public List<ClassStudentOnlineTest> exportStudentScoresInfo(int onlinetestID, int[] classValue) {

        List<Integer> classList = new ArrayList<>();
        for (int i = 0; i < classValue.length; i++) {
            classList.add(classValue[i]);
        }
        List<ClassStudentOnlineTest> classStudents =  teacherCheckOnlineTestMapper.exportStudentScoresInfo(onlinetestID, classList);
        return classStudents;

    }

    public String getStudentJudge(int onlinetestID) {

        return teacherCheckOnlineTestMapper.getStudentJudge(onlinetestID);

    }
}
