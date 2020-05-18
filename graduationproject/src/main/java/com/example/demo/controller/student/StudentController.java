package com.example.demo.controller.student;

import com.example.demo.bean.*;
import com.example.demo.common.UserUtils;
import com.example.demo.service.student.StudentService;
import org.jodconverter.DocumentConverter;
import org.jodconverter.LocalConverter;
import org.jodconverter.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    DocumentConverter converter;

    @Value("${file.rootPath}")
    private String ROOT_PATH;
    //图片存放根目录下的子目录
    @Value("${file.sonPath}")
    private String SON_PATH;

    /*获得学生的作业*/
    @RequestMapping(value = "/gethomworkinfoandState", method = RequestMethod.GET)
    public List<Homework> getHomworkInfoAndState(String studentID, int courseID) {
        return studentService.getHomworkInfoAndState(studentID, courseID);
    }

    @RequestMapping(value = "/getexaminationinfoandstate", method = RequestMethod.GET)
    public List<Examination> getExaminationInfoAndState(String studentID, int courseID){
        return studentService.getExaminationInfoAndState(studentID, courseID);
    }

    /*获得学生所学学科*/
    @RequestMapping(value = "/getsubjectsbystudentid", method = RequestMethod.GET)
    public Map<String, Object> getSubjectsByTeacherId(String studentID) {
        Map<String, Object> map = new HashMap<>();
        List<Course> courseList = studentService.getSubjectsByStudentId(studentID);
        map.put("subject", courseList);
        return map;
    }

    /*上传学生的作业*/
    @RequestMapping(value = "/uploadhomework", method = RequestMethod.POST)
    public RespBean homeworkUpload(int homeworkID, String major, String grade,
                                   String sclass, int classID, MultipartFile file) throws IOException, OfficeException {
        if(file.isEmpty()){
            return RespBean.error("文件为空，请重新提交");
        }
        System.out.println("================================" + homeworkID + "=============================");

        String fileName = file.getOriginalFilename(); //获得文件名，前端已经判断好了文件名，无需再次判断
        String filePName = fileName.substring(0, fileName.indexOf('.'));
        String mgc = major + grade + sclass + "/" + classID;
        File dest = new File(ROOT_PATH + SON_PATH + homeworkID + "/" + mgc + "/" + UserUtils.getCurrentUser().getUser_id() + "/" + fileName);
        File destpdf = new File(ROOT_PATH + SON_PATH + homeworkID + "/" + mgc + "/" + UserUtils.getCurrentUser().getUser_id() + "/" + filePName + ".pdf");
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        deleteDirectory(dest.getParentFile());
        deleteDirectory(destpdf.getParentFile());
        file.transferTo(dest);

        //转化成pdf存储
        converter.convert(dest).to(destpdf).execute();
        System.out.println("===================================pdf转化成功============================");
        String path = SON_PATH  + homeworkID + "/" + mgc + "/" + UserUtils.getCurrentUser().getUser_id() + "/" + fileName;
        boolean result = studentService.homeworkUpload(UserUtils.getCurrentUser().getUser_id(), path, homeworkID);

        System.out.println("===============================" + result + "====================");

        if(result) {
            return RespBean.ok("作业上传成功");
        }else{
            return RespBean.error("作业上传失败");
        }
    }

    @RequestMapping(value = "/downloadyourwork", method = RequestMethod.POST)
    public Object downloadFile(@RequestParam("formData") String formData) throws IOException {

        return studentService.downloadFile(formData);

    }

    @RequestMapping(value = "/submitCode", method = RequestMethod.POST)
    public RespBean SubmitCode(String answerStudent, String studentID, int examinationID){

        boolean flag = studentService.SubmitCode(answerStudent, studentID, examinationID);

        if(flag){
            return RespBean.ok("代码提交成功");
        }else{
            return RespBean.error("代码提交失败");
        }

    }

    /*获得学生专业年级班级*/
    @RequestMapping(value = "/getmgcbystudentid", method = RequestMethod.GET)
    public Map<String,Object> getMajorGradeClassByStudentId(String studentID){

        Map<String,Object> map = new HashMap<>();
        Major major = studentService.getMajorGradeClassByStudentId(studentID);
        map.put("studentMajor", major);
        return map;
    }

    private static void deleteDirectory(File file) {
        if (file.isFile()) {// 表示该文件不是文件夹
            file.delete();
        } else {
            // 首先得到当前的路径
            String[] childFilePaths = file.list();
            for (String childFilePath : childFilePaths) {
                File childFile = new File(file.getAbsolutePath() + "/" + childFilePath);
                deleteDirectory(childFile);
            }
        }
    }



}

