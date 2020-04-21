package com.example.demo.service.schedule;

import com.example.demo.mapper.schedule.ScheduledMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ScheduledService {

    @Resource
    ScheduledMapper scheduledMapper;

    public void deleteUselessInfo() {

        /*管理考试方面的内容*/
        List<Integer> ects = scheduledMapper.getEctId();
        List<Integer> eids = scheduledMapper.getEId();
        for(Integer eid : eids){

            if(ects.contains(eid)){
                continue;
            }else{
                scheduledMapper.deleteEid(eid);
            }

        }

        /*管理作业方面的内容*/
        List<Integer> hcts = scheduledMapper.getHctId();
        List<Integer> hids = scheduledMapper.getHId();
        for(Integer hid : hids){

            if(hcts.contains(hid)){
                continue;
            }else{
                scheduledMapper.deleteHid(hid);
            }

        }

        /*管理学生方面的内容*/
        /*获取了user表中的id*/
        List<String> studentsId = scheduledMapper.getStudentsIdbyrole();
        /*获取了student表中的id*/
        List<String> stduentsId2 = scheduledMapper.getStudentsIdbyinfo();
        for(String studentId: studentsId){

            if(stduentsId2.contains(studentId)){
                continue;
            }else{
                scheduledMapper.deleteStudentId(studentId);
            }

        }

        /*管理教师方面的内容*/
        /*获取了user表中的id*/
        List<String> teachersId = scheduledMapper.getTeachersIdbyrole();
        /*获取了teacher表中的id*/
        List<String> teachersId2 = scheduledMapper.getTeachersIdbyinfo();
        for(String teacherId: teachersId){

            if(teachersId2.contains(teacherId)){
                continue;
            }else{
                scheduledMapper.deleteTeacherId(teacherId);
            }

        }



    }

}
