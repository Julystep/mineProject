package com.example.demo.mapper.teacher;

import com.example.demo.bean.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeacherHomeworkMapper {


    /*获取教师所教课程*/
    List<Course> getSubjectsByTeacherId(String teacherID);

    /*获得学习本门课程的学生2*/
    List<Major> getClassesByCourseId2(int courseID);

    /*存入作业并且返回id*/
    boolean onSubmitHomework(@Param("homework") Homework homework);

    /*将作业和班级绑定*/
    boolean setHomeworkWithClass(@Param("classIDList") List<Integer> classIDList,
                                 @Param("homeworkID") int homeworkID);

    /*将作业和老师课程绑定*/
    boolean setHomeworkWithTeacherAndCourse(@Param("teacherID") String teacherID,
                                            @Param("courseID") int courseID,
                                            @Param("homeworkID") int homworkID);

    /*获得学生信息*/
    List<String> getSudentIdsByClasses(int homeworkID);

    /*将学生和作业信息插入homework_class表格中*/
    boolean setStudentInfoInHomeworkClass(@Param("studentID") List<String> studentID,
                                          @Param("homeworkID") int homeworkID);
}
