package com.example.demo.controller.teacher;

import com.example.demo.bean.RespBean;
import com.example.demo.service.teacher.TeacherExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher")
public class TeacherExaminationController {

    @Autowired
    TeacherExaminationService teacherExaminationService;

    /* 教师发布考试内容 */
    @RequestMapping(value = "/subexamination", method = RequestMethod.POST)
    public RespBean onSubmitExamination(@RequestParam("form") String form){
        boolean flag = teacherExaminationService.onSubmitExamination(form);

        if(flag){
            return RespBean.ok("作业发布成功");
        }else{
            return RespBean.error("作业发布失败");
        }
    }
}
