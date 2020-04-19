package com.example.demo.controller.teacher;

import com.example.demo.bean.ClassStudent;
import com.example.demo.bean.Homework;
import com.example.demo.bean.RespBean;
import com.example.demo.common.PoiUtils;
import com.example.demo.service.teacher.TeacherCheckHomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherCheckHomeworkController {

    @Autowired
    TeacherCheckHomeworkService teacherCheckHomeworkService;

    @RequestMapping(value = "/gethomeworkinfobyteacheridandcourseid", method = RequestMethod.GET)
    public List<Homework> getHomeworkInfoByTeacherIdAndCourseId(String teacherID, int courseID){
        return teacherCheckHomeworkService.getHomeworkInfoByTeacherIdAndCourseId(teacherID, courseID);
    }

    /**
     *
     * @param homework       作业内容
     * @param classValue     选择的班级的内容
     * @return               返回查询学生的结果集
     */
    @RequestMapping(value = "/getSubmitStudent", method = RequestMethod.POST)
    public Map<String, Object> getHomeworkDetail(@RequestParam("homework") String homework,
                                                 @RequestParam("classValue") int[] classValue,
                                                 @RequestParam("studentInfo") String studentInfo,
                                                 @RequestParam("stateChecked") boolean stateChecked,
                                                 @RequestParam("commentChecked") boolean commentChecked,
                                                 @RequestParam("allChecked") boolean allChecked,
                                                 @RequestParam("page") int page,
                                                 @RequestParam("size") int size
                                                 ){

        Map<String, Object> map = new HashMap<>();
        map = teacherCheckHomeworkService.getHomeworkDetail(homework, classValue, studentInfo, stateChecked, commentChecked, allChecked, page, size);

        return map;

    }
    @RequestMapping(value = "/gethomeworkdetailcount", method = RequestMethod.POST)
    public Map<String, Object> getHomeworkDetailCount(@RequestParam("homeworkID") int homeworkID,
                                                      @RequestParam("classValue") int[] classValue){

        Map<String, Object> map = new HashMap<>();
        map = teacherCheckHomeworkService.getHomeworkDetailCount(homeworkID, classValue);

        return map;
    }

    @RequestMapping(value = "/changehoeworkmain", method = RequestMethod.POST)
    public RespBean changeHomeworkMain(@RequestParam("homeworkID") int homeworkID,
                                       @RequestParam("main") String main){

        if(teacherCheckHomeworkService.changeHomeworkMain(homeworkID, main)){
            return RespBean.ok("修改作业内容成功");
        }else{
            return RespBean.error("修改作业内容失败");
        }

    }

    @RequestMapping(value = "/changecomment", method = RequestMethod.POST)
    public RespBean changeComment(@RequestParam("homeworkID") int homeworkID,
                                  @RequestParam("studentID") String studentID,
                                  @RequestParam("main") String main){

        if(teacherCheckHomeworkService.changeComment(homeworkID, studentID, main)){
            return RespBean.ok("修改教师评语成功");
        }else{
            return RespBean.error("修改教师评语失败");
        }

    }

    @RequestMapping(value = "/changestudentscores", method = RequestMethod.POST)
    public RespBean changeStudentScores(@RequestParam("judgeStudent") String judgeStudent,
                                        @RequestParam("studentID") String studentID,
                                        @RequestParam("homeworkID") int homeworkID,
                                        @RequestParam("commentValue") String commentValue){
        System.out.println("================================" + commentValue);

        if(teacherCheckHomeworkService.changeStudentScores(judgeStudent, studentID, homeworkID, commentValue)){
            return RespBean.ok("修改学生成绩成功");
        }else{
            return RespBean.error("修改学生成绩失败");
        }
    }

    @RequestMapping(value = "/changehomeworkdate", method = RequestMethod.POST)
    public RespBean changeHomeworkDate(@RequestParam("homeworkID") int homeworkID,
                                       @RequestParam("dateValue") Date[] dateValue){


        /*for(int i = 0; i < dateValue.length; i++){
            System.out.println(dateValue[i]);
        }*/
        if(teacherCheckHomeworkService.changeHomeworkDate(homeworkID, dateValue)){
            return RespBean.ok("修改作业提交时间成功");
        }else{
            return RespBean.error("修改作业提交时间失败");
        }
    }

    @RequestMapping(value = "/deletehomework", method = RequestMethod.POST)
    public RespBean deleteHomework(@RequestParam("homeworkID") int homeworkID){

        if(teacherCheckHomeworkService.deletehomework(homeworkID)){
            return RespBean.ok("作业信息删除成功");
        }else{
            return RespBean.error("作业信息删除失败");
        }

    }

    @RequestMapping(value = "/exportStudent", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportStudent(@RequestParam("homeworkID") int homeworkID,
                                        @RequestParam("classValue") int[] classValue) throws Exception {

        return PoiUtils.exportEmp2Excel(teacherCheckHomeworkService.exportStudentScoresInfo(homeworkID, classValue), teacherCheckHomeworkService.getStudentJudge(homeworkID));

    }

}
