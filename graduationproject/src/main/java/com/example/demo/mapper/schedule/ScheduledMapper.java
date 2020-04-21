package com.example.demo.mapper.schedule;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduledMapper {


    List<Integer> getEctId();

    List<Integer> getEId();

    void deleteEid(Integer eid);

    List<Integer> getHctId();

    List<Integer> getHId();

    void deleteHid(Integer hid);

    List<String> getStudentsIdbyrole();

    List<String> getStudentsIdbyinfo();

    void deleteStudentId(String studentId);

    List<String> getTeachersIdbyrole();

    List<String> getTeachersIdbyinfo();

    void deleteTeacherId(String teacherId);
}
