package com.example.demo.mapper.teacher;

import com.example.demo.bean.ClassStudentOnlineTest;
import com.example.demo.bean.Examination;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface TeacherCheckOnlineTestMapper {
    List<Examination> getOnlineTestInfoByTeacherIdAndCourseId(String teacherID, int courseID);

    List<ClassStudentOnlineTest> getOnlineTestDetail(int onlinetestID, List<Integer> classList, String studentInfo, boolean stateChecked, boolean commentChecked, boolean allChecked, int page, int size);

    int getOnlineTestCount(int onlinetestID, List<Integer> classList, String studentInfo, boolean stateChecked, boolean commentChecked, boolean allChecked);

    int getSubmitCount(int onlinetestID, List<Integer> classList);

    int getNoCheckedCount(int onlinetestID, List<Integer> classList);

    int getNoSubmitCount(int onlinetestID, List<Integer> classList);

    boolean changeOnlineTestDate(int onlinetestID, Date sdate, Date edate);

    boolean deleteOnlineTest(int onlinetestID);

    boolean changeStudentexaminationScores(String answer, String studentID, int homeworkID, String commentValue);

    boolean changeExaminationComment(int homeworkID, String studentID, String main);
}
