package com.example.demo.service.teacher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bean.Examination;
import com.example.demo.mapper.teacher.TeacherExaminationMapper;
import com.example.demo.mapper.teacher.TeacherHomeworkMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TeacherExaminationService {

    @Resource
    TeacherExaminationMapper teacherExaminationMapper;


    public boolean onSubmitExamination(String form) {

        JSONObject jsonObject = JSON.parseObject(form);
        String teacherID = (String) jsonObject.get("teacherID");
        String title = jsonObject.getString("title");
        String main = jsonObject.getString("main");
        JSONArray jsonArray = jsonObject.getJSONArray("dateValue");
        Date sdate = jsonArray.getDate(0);
        Date edate = jsonArray.getDate(1);
        int courseID = jsonObject.getInteger("courseID");
        /*获取需要完成该作业的班级*/
        JSONArray jsonArray1 = jsonObject.getJSONArray("classValue");
        List<Integer> classIDList = new ArrayList<>();
        JSONArray jsonArray2 = null;
        for(int i = 0; i < jsonArray1.size(); i++){
            jsonArray2  = jsonArray1.getJSONArray(i);
            classIDList.add(jsonArray2.getInteger(Integer.parseInt("2")));
        }
        Examination examination = new Examination();
        examination.setExamination_title(title);
        examination.setExamination_main(main);
        examination.setSdate(sdate);
        examination.setEdate(edate);

        if(teacherExaminationMapper.onSubmitExamination(examination)){
            // 将考试和老师和课程绑定
            // 将考试和班级绑定
            // 将考试以状态形式和学生绑定
            if(teacherExaminationMapper.setExaminationWithClass(classIDList, examination.getId())){
                List<String> studentID = teacherExaminationMapper.getSudentIdsByClasses(examination.getId());
                if(studentID.size() == 0){
                    return false;
                }
                if(teacherExaminationMapper.setStudentInfoInExaminationClass(studentID, examination.getId()) &&
                   teacherExaminationMapper.setExaminationWithTeacherAndCourse(teacherID, courseID, examination.getId())){

                    return true;

                }
            }
        }
        return false;

    }

}
