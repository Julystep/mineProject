package com.example.demo.service.teacher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bean.ClassStudent;
import com.example.demo.bean.Homework;
import com.example.demo.mapper.teacher.TeacherCheckHomeworkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TeacherCheckHomeworkService {

    @Resource
    TeacherCheckHomeworkMapper teacherCheckHomeworkMapper;

    public List<Homework> getHomeworkInfoByTeacherIdAndCourseId(String teacherID, int courseID) {
        return teacherCheckHomeworkMapper.getHomeworkInfoByTeacherIdAndCourseId(teacherID, courseID);
    }

    /**
     *
     * @param homework       作业内容
     * @param classValue     选择的班级的内容
     * @return               返回查询学生的结果集
     */
    public Map<String, Object> getHomeworkDetail(String homework,
                                                int[]classValue,
                                                String studentInfo,
                                                boolean stateChecked,
                                                boolean commentChecked,
                                                boolean allChecked,
                                                int page,
                                                int size) {
        page = (page - 1) * 20;
        JSONObject jsonObject = JSON.parseObject(homework);
        /*获得作业id*/
        int homeworkID = jsonObject.getInteger("id");
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
        map.put("classStudents", teacherCheckHomeworkMapper.getHomeworkDetail(homeworkID, classList, studentInfo, stateChecked, commentChecked, allChecked, page, size));
        map.put("studentsCount", teacherCheckHomeworkMapper.getHomeworkCount(homeworkID, classList, studentInfo, stateChecked, commentChecked, allChecked));
        return map;
    }


    public Map<String, Object> getHomeworkDetailCount(int homeworkID, int[] classValue){
        List<Integer> classList = new ArrayList<>();
        if(classValue.length != 0) {
            for(int i = 0; i < classValue.length; i++){
                classList.add(classValue[i]);
            }
        }
        Map<String, Object> map = new HashMap<>();
        int sumbitCount = teacherCheckHomeworkMapper.getSubmitCount(homeworkID, classList);
        int noCheckedCount = teacherCheckHomeworkMapper.getNoCheckedCount(homeworkID, classList);
        int noSumbitCount = teacherCheckHomeworkMapper.getNoSubmitCount(homeworkID, classList);
        System.out.println(sumbitCount + "   " + noCheckedCount + "   " + noSumbitCount);
        map.put("submitCount", sumbitCount);
        map.put("noCheckedCount", noCheckedCount);
        map.put("noSubmitCount", noSumbitCount);
        return map;
    }

    public boolean changeHomeworkMain(int homeworkID, String main) {
        return teacherCheckHomeworkMapper.changeHomeworkMain(homeworkID, main);
    }

    public boolean changeComment(int homeworkID, String studentID, String main) {
        return teacherCheckHomeworkMapper.changeComment(homeworkID, studentID, main);
    }

    public boolean changeStudentScores(String judgeStudent, String studentID, int homeworkID, String commentValue) {
        return teacherCheckHomeworkMapper.changeStudentScores(judgeStudent, studentID, homeworkID, commentValue);
    }

    public boolean changeHomeworkDate(int homeworkID, Date[] dateValue) {
        Date sdate = dateValue[0];
        Date edate = dateValue[1];
        return teacherCheckHomeworkMapper.changeHomeworkDate(homeworkID, sdate, edate);
    }

    public boolean deletehomework(int homeworkID) {

        return  teacherCheckHomeworkMapper.deleteHomeworkInfo(homeworkID);
    }

    public List<ClassStudent> exportStudentScoresInfo(int homeworkID, int[] classValue){
        List<Integer> classList = new ArrayList<>();
        for (int i = 0; i < classValue.length; i++) {
            classList.add(classValue[i]);
        }
        List<ClassStudent> classStudents =  teacherCheckHomeworkMapper.exportStudentScoresInfo(homeworkID, classList);
        for(ClassStudent classStudent : classStudents){
            System.out.println(classStudent);
        }
        return classStudents;
    }

    public String getStudentJudge(int homeworkID){
        return teacherCheckHomeworkMapper.getStudentJudge(homeworkID);
    }
}
