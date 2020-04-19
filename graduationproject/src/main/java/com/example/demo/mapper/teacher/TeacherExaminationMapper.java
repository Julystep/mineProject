package com.example.demo.mapper.teacher;

import com.example.demo.bean.Examination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeacherExaminationMapper {

    boolean onSubmitExamination(@Param("examination") Examination examination);

    /*绑定班级和考试内容*/
    boolean setExaminationWithClass(List<Integer> classIDList, int id);

    /*获取学生信息*/
    List<String> getSudentIdsByClasses(int id);

    /*绑定学生-考试*/
    boolean setStudentInfoInExaminationClass(List<String> studentID, int id);

    /*绑定教师-课程-考试*/
    boolean setExaminationWithTeacherAndCourse(String teacherID, int courseID, int id);
}
