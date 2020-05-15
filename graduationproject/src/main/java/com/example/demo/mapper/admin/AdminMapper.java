package com.example.demo.mapper.admin;

import com.example.demo.bean.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface AdminMapper {

    Admin getMajorID(String userID);

    List<Student> getStudentByMajorID(int majorID, int page, int size, List<Integer> classIDList, String studentInfo);

    List<Major> getClassesByMajorId(int majorID);

    boolean insertUser(@Param("student") Student student);

    boolean updateUser(@Param("student") Student student);

    boolean deleteStudentInfoDetail(String userID);

    boolean insertTUser(@Param("teacher") Teacher teacher);

    boolean userTRole(@Param("teacher") Teacher teacher);

    boolean insertTUser_info(@Param("teacher") Teacher teacher);

    boolean insertUser_info(@Param("student") Student student);

    boolean updateUser_info(@Param("student") Student student);

    boolean userRole(@Param("student") Student student);

    Student getUserByUserID(String user_id);

    int getStudentByMajorIDCounts(int majorID, int page, int size, List<Integer> classIDList, String studentInfo);

    List<Major> getAllGrade(@Param("majorList") List<Integer> list);

    boolean updateGrade(int id, String label);

    boolean updateClass(int id, String label);

    boolean deleteGrade(int grade_id);

    boolean deleteClass(int class_id);

    List<Major> getMajorAndGrade(@Param("majorList") List<Integer> majorList);

    boolean insertGrade(int id, Set<String> set);

    boolean insertClass(int id, Set<String> set);

    List<Grade> getAllGradeWithNone();

    List<Sclass> getAllClassWithNone();

    List<Sclass> getClassesById(Integer grade_id);

    List<Grade> getGradesById(Integer major_id);

    String getClassIDByName(@Param("student") Student student);

    boolean importStudents(List<Student> students);

    boolean importStudentsInfo(List<Student> students);

    List<Teacher> getTeacherByMajorID(int majorID, int page, int size, List<Integer> majorIDList, String teacherInfo);

    int getTeacherByMajorIDCounts(int majorID, int page, int size, List<Integer> majorIDList, String teacherInfo);


    boolean updateTUser(@Param("teacher") Teacher teacher);

    boolean updateTUser_info(@Param("teacher") Teacher teacher);

    boolean deleteTeacherInfoDetail(String userID);

    boolean importStudentsRole(List<Student> students);

    Teacher getTeacherByUserID(String user_id);

    String getMajorIDByName(@Param("teacher") Teacher teacher);

    boolean importTeachers(List<Teacher> teachers);

    boolean importTeachersInfo(List<Teacher> teachers);

    boolean importTeachersRole(List<Teacher> teachers);

    List<Major> getAllCourse(List<Integer> list);

    List<Course> getCoursesById(Integer major_id);

    boolean updateCourse(int id, String label);

    boolean deleteCourse(int course_id);

    List<Course> getAllCourseWithNone();

    boolean insertCourse(int id, Set<String> set);

    List<Major> getClassByMajorList(List<Integer> list);

    List<Major> getAllteachers();

    boolean insertCourseAndClass(@Param("classList") List<Integer> classList, int course_id);

    boolean insertCourseAndTeacher(String teacher_id, int course_id);

    boolean testClassAndCourse(Integer integer, int course_id);

    boolean testTeacherAndCourse(String teacher_id, int course_id);

    List<MGCBean> getClasses(int course_id);

    Teacher getTeacher(int course_id);

    boolean deleteCourseClassConnect(int course_id);

    boolean deleteCourseTeacherConnect(int course_id);

    boolean changePassword(String user_id, String passwordEncode);

    boolean changeCoursePictureById(int id, String path);
}
