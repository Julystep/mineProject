package com.example.demo.service.student;

import com.example.demo.bean.Course;
import com.example.demo.bean.Examination;
import com.example.demo.bean.Homework;
import com.example.demo.bean.Major;
import com.example.demo.mapper.student.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentService {

    @Resource
    StudentMapper studentMapper;

    public List<Homework> getHomworkInfoAndState(String studentID, int courseID){
        return studentMapper.getHomworkInfoAndState(studentID, courseID);
    }

    public List<Course> getSubjectsByStudentId(String studentID) {
        /*先获取学生所在班级*/
        int classID = studentMapper.getClassIdByStudentId(studentID);
        /*通过班级id获得学科*/
        return studentMapper.getSubjectsByClassId(classID);
    }

    /*获得学生专业年级班级*/
    public Major getMajorGradeClassByStudentId(String studentID){
        return studentMapper.getMajorGradeClassByStudentId(studentID);
    }

    public boolean homeworkUpload(String id, String path, int homeworkID) {
        return studentMapper.homeworkUpload(id, path, homeworkID);
    }

    public List<Examination> getExaminationInfoAndState(String studentID, int courseID) {
        return studentMapper.getExaminationInfoAndState(studentID, courseID);
    }

    public boolean SubmitCode(String answerStudent, String studentID, int examinationID) {

        boolean flag = studentMapper.SubmitCode(answerStudent, studentID, examinationID);
        return flag;

    }
}
