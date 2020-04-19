package com.example.demo.mapper.teacher;

import com.example.demo.bean.ClassStudent;
import com.example.demo.bean.Homework;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface TeacherCheckHomeworkMapper {
    public List<Homework> getHomeworkInfoByTeacherIdAndCourseId(String teacherID, int courseID);

    List<ClassStudent> getHomeworkDetail(int homeworkID,
                                         List<Integer> classList,
                                         String studentInfo,
                                         boolean stateChecked,
                                         boolean commentChecked,
                                         boolean allChecked,
                                         int page,
                                         int size);

    int getHomeworkCount(int homeworkID,
                               List<Integer> classList,
                               String studentInfo,
                               boolean stateChecked,
                               boolean commentChecked,
                               boolean allChecked);

    int getSubmitCount(int homeworkID, List<Integer> classList);

    int getNoCheckedCount(int homeworkID, List<Integer> classList);

    int getNoSubmitCount(int homeworkID, List<Integer> classList);

    boolean changeHomeworkMain(int homeworkID, String main);

    boolean changeComment(int homeworkID, String studentID, String main);

    boolean changeStudentScores(String judgeStudent, String studentID, int homeworkID, String commentValue);

    boolean changeHomeworkDate(int homeworkID, Date sdate, Date edate);

    /*以下负责彻底删除作业*/

    boolean deleteHomeworkInfo(int homeworkID);

    /*导出学生的某次作业的成绩单*/

    List<ClassStudent> exportStudentScoresInfo(int homeworkID, List<Integer> classList);

    String getStudentJudge(int homeworkID);

}
