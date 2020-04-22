package com.example.demo.controller.teacher;

import com.example.demo.bean.Examination;
import com.example.demo.bean.RespBean;
import com.example.demo.common.PoiUtils;
import com.example.demo.service.teacher.TeacherCheckOnlineTestService;
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
public class TeacherCheckOnlineTestController {

    @Autowired
    TeacherCheckOnlineTestService teacherCheckOnlineTestService;

    @RequestMapping(value = "/getonlinetestinfobyteacheridandcourseid", method = RequestMethod.GET)
    public List<Examination> getOnlineTestInfoByTeacherIdAndCourseId(String teacherID, int courseID){
        return teacherCheckOnlineTestService.getOnlineTestInfoByTeacherIdAndCourseId(teacherID, courseID);
    }

    /**
     *
     * @param onlinetest        在线考试内容
     * @param classValue        班级内容
     * @param studentInfo       学生信息
     * @param stateChecked      作业检查情况
     * @param commentChecked    是否被评测
     * @param allChecked        是否被检查
     * @param page              当前页
     * @param size              页面大小
     * @return                  返回该页学生信息
     */
    @RequestMapping(value = "/getSubmitonlinetestStudent", method = RequestMethod.POST)
    public Map<String, Object> getOnlineTestDetail(@RequestParam("onlinetest") String onlinetest,
                                                 @RequestParam("classValue") int[] classValue,
                                                 @RequestParam("studentInfo") String studentInfo,
                                                 @RequestParam("stateChecked") boolean stateChecked,
                                                 @RequestParam("commentChecked") boolean commentChecked,
                                                 @RequestParam("allChecked") boolean allChecked,
                                                 @RequestParam("page") int page,
                                                 @RequestParam("size") int size
    ){

        Map<String, Object> map = new HashMap<>();
        map = teacherCheckOnlineTestService.getOnlineTestDetail(onlinetest, classValue, studentInfo, stateChecked, commentChecked, allChecked, page, size);

        return map;

    }

    @RequestMapping(value = "/getonlinetestdetailcount", method = RequestMethod.POST)
    public Map<String, Object> getOnlineTestDetailCount(@RequestParam("onlinetestID") int onlinetestID,
                                                      @RequestParam("classValue") int[] classValue){

        Map<String, Object> map = new HashMap<>();
        map = teacherCheckOnlineTestService.getOnlineTestDetailCount(onlinetestID, classValue);

        return map;
    }

    @RequestMapping(value = "/changeonlinetestdate", method = RequestMethod.POST)
    public RespBean changeOnlineTestDate(@RequestParam("onlinetestID") int onlinetestID,
                                       @RequestParam("dateValue") Date[] dateValue){


        /*for(int i = 0; i < dateValue.length; i++){
            System.out.println(dateValue[i]);
        }*/
        if(teacherCheckOnlineTestService.changeOnlineTestDate(onlinetestID, dateValue)){
            return RespBean.ok("修改作业提交时间成功");
        }else{
            return RespBean.error("修改作业提交时间失败");
        }
    }

    @RequestMapping(value = "/deleteonlineTest", method = RequestMethod.POST)
    public RespBean deleteOnlineTest(@RequestParam("onlinetestID") int onlinetestID){

        if(teacherCheckOnlineTestService.deleteOnlineTest(onlinetestID)){
            return RespBean.ok("作业信息删除成功");
        }else{
            return RespBean.error("作业信息删除失败");
        }

    }

    @RequestMapping(value = "/changestudentsexaminationcores", method = RequestMethod.POST)
    public RespBean changeStudentScores(@RequestParam("answer") String answer,
                                        @RequestParam("studentID") String studentID,
                                        @RequestParam("onlinetestID") int homeworkID,
                                        @RequestParam("commentValue") String commentValue){

        if(teacherCheckOnlineTestService.changeStudentexaminationScores(answer, studentID, homeworkID, commentValue)){
            return RespBean.ok("修改学生成绩成功");
        }else{
            return RespBean.error("修改学生成绩失败");
        }
    }

    @RequestMapping(value = "/changeexaminationcomment", method = RequestMethod.POST)
    public RespBean changeExaminationComment(@RequestParam("onlinetestID") int homeworkID,
                                  @RequestParam("studentID") String studentID,
                                  @RequestParam("main") String main){

        if(teacherCheckOnlineTestService.changeExaminationComment(homeworkID, studentID, main)){
            return RespBean.ok("修改教师评语成功");
        }else{
            return RespBean.error("修改教师评语失败");
        }

    }

    @RequestMapping(value = "/exportStudentTest", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportTeacher(@RequestParam("onlinetestID") int onlinetestID,
                                                @RequestParam("classValue") int[] classValue) throws Exception {

        return PoiUtils.exportStudentTestExcel(teacherCheckOnlineTestService.exportStudentScoresInfo(onlinetestID, classValue), teacherCheckOnlineTestService.getStudentJudge(onlinetestID));

    }

}
