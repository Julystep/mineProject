package com.example.demo.mapper.student;

import com.example.demo.bean.Course;
import com.example.demo.bean.Examination;
import com.example.demo.bean.Homework;
import com.example.demo.bean.Major;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {

    List<Homework> getHomworkInfoAndState(String studentID, int courseID);

    List<Course> getSubjectsByClassId(int classID);

    int getClassIdByStudentId(String studentID);

    Major getMajorGradeClassByStudentId(String studentID);

    boolean homeworkUpload(String id, String path, int homeworkID);

    List<Examination> getExaminationInfoAndState(String studentID, int courseID);

    boolean SubmitCode(String answerStudent, String studentID, int examinationID);
}
