package com.example.demo.service.student;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bean.Course;
import com.example.demo.bean.Examination;
import com.example.demo.bean.Homework;
import com.example.demo.bean.Major;
import com.example.demo.common.UserUtils;
import com.example.demo.mapper.student.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

@Service
public class StudentService {

    @Value("${file.rootPath}")
    private String ROOT_PATH;
    //图片存放根目录下的子目录
    @Value("${file.sonPath}")
    private String SON_PATH;

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

    /*@Async("taskExecutor")*/
    public boolean SubmitCode(String answerStudent, String studentID, int examinationID) {

        boolean flag = studentMapper.SubmitCode(answerStudent, studentID, examinationID);
        return flag;

    }

    public Object downloadFile(String formData) throws IOException {

        JSONObject jsonObject = JSON.parseObject(formData);
        int homeworkID = jsonObject.getInteger("homeworkID");
        String major = jsonObject.getString("major");
        String grade = jsonObject.getString("grade");
        String sclass = jsonObject.getString("sclass");
        String classID = jsonObject.getString("classID");
        String mgc = major + grade + sclass + "/" + classID;
        File dest = new File(ROOT_PATH + SON_PATH + homeworkID + "/" + mgc + "/" + UserUtils.getCurrentUser().getUser_id());
        File []files = dest.listFiles();
        for(File file: files){
            if(file.getName().endsWith(".doc") || file.getName().endsWith(".docx")){
                FileInputStream fileInputStream = new FileInputStream(file);
                System.out.println(fileInputStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream(fileInputStream.available());
                byte[] bytes = new byte[fileInputStream.available()];
                int temp;
                while ((temp = fileInputStream.read(bytes)) != -1) {
                    baos.write(bytes, 0, temp);
                }
                byte[] buffer;
                fileInputStream.close();
                baos.close();
                buffer = baos.toByteArray();
                return buffer;
            }
        }
        return null;
    }

    public static void main(String []args) throws IOException {

        File file = new File("E://homework/33/电气及其自动化2016级1班/9/16033/160405214李明磊.docx");
        FileInputStream fileInputStream = new FileInputStream(file);
        System.out.println(fileInputStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(fileInputStream.available());
        byte[] bytes = new byte[fileInputStream.available()];
        int temp;
        while ((temp = fileInputStream.read(bytes)) != -1) {
          baos.write(bytes, 0, temp);
        }
        byte[] buffer;
        fileInputStream.close();
        baos.close();
        buffer = baos.toByteArray();
        System.out.println(buffer);
    }

}
