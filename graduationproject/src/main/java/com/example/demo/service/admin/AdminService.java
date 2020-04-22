package com.example.demo.service.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bean.*;
import com.example.demo.mapper.admin.AdminMapper;
import com.example.demo.mapper.root.RootMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class AdminService {

    @Resource
    AdminMapper adminMapper;

    @Resource
    RootMapper rootMapper;

    public Admin getMajorID(String userID) {
        return adminMapper.getMajorID(userID);
    }

    public Map<String, Object> getStudentByMajor(String majorList, int page, int size, String form) {
        page = (page - 1) * 20;
        int majorID;
        String majorName;
        Map<String, Object> map = new HashMap<String, Object>();
        JSONArray majorArray = JSON.parseArray(majorList);
        System.out.println(form);
        JSONObject formData = JSON.parseObject(form);
        JSONArray jsonArray1 = formData.getJSONArray("classValue");
        List<Integer> classIDList = new ArrayList<>();
        JSONArray jsonArray2 = null;
        for(int i = 0; i < jsonArray1.size(); i++){
            jsonArray2  = jsonArray1.getJSONArray(i);
            classIDList.add(jsonArray2.getInteger(Integer.parseInt("2")));
        }
        String studentInfo = formData.getString("userInfo");


        for(int i = 0; i < majorArray.size(); i++){
            List<Student> students = null;
            JSONObject jsonObject = majorArray.getJSONObject(i);
            majorID = jsonObject.getInteger("major_id");
            majorName = jsonObject.getString("major_name");
            students = adminMapper.getStudentByMajorID(majorID, page, size, classIDList, studentInfo);
            int studentsCount = adminMapper.getStudentByMajorIDCounts(majorID, page, size, classIDList, studentInfo);
            map.put(majorName, students);
            map.put(majorName+"count", studentsCount);
        }


        return map;

    }

    public Map<String, Object> getTeacherByMajor(String majorList, int page, int size, String form) {

        page = (page - 1) * 20;
        int majorID;
        String majorName;
        Map<String, Object> map = new HashMap<String, Object>();
        JSONArray majorArray = JSON.parseArray(majorList);
        JSONObject formData = JSON.parseObject(form);
        JSONArray jsonArray1 = formData.getJSONArray("majorValue");
        List<Integer> majorIDList = new ArrayList<>();
        JSONArray jsonArray2 = null;
        for(int i = 0; i < jsonArray1.size(); i++){
            majorIDList.add(jsonArray2.getInteger(Integer.parseInt("2")));
        }
        String teacherInfo = formData.getString("userInfo");


        for(int i = 0; i < majorArray.size(); i++){
            List<Teacher> teachers = null;
            JSONObject jsonObject = majorArray.getJSONObject(i);
            majorID = jsonObject.getInteger("major_id");
            majorName = jsonObject.getString("major_name");
            teachers = adminMapper.getTeacherByMajorID(majorID, page, size, majorIDList, teacherInfo);
            int teachersCount = adminMapper.getTeacherByMajorIDCounts(majorID, page, size, majorIDList, teacherInfo);
            map.put(majorName, teachers);
            map.put(majorName+"count", teachersCount);
        }


        return map;

    }


    public List<Major> getClassesByMajorId(int majorID) {

        return adminMapper.getClassesByMajorId(majorID);

    }

    public boolean insertStudentInfoDetail(String form) {

        List<String> ids = rootMapper.getAllID();

        JSONObject formData = JSON.parseObject(form);
        /*获取id*/
        String user_id = formData.getString("user_id");
        if(ids.contains(user_id)){
            return false;
        }
        /*获取用户名称*/
        String username = formData.getString("username");
        /*获取用户邮箱*/
        String email = formData.getString("email");
        /*获取用户电话*/
        String phone = formData.getString("phone");
        /*获取专业管理员参与管理的专业*/
        JSONArray jsonArray = formData.getJSONArray("classValue");
        int classId = jsonArray.getInteger(2);

        System.out.println(classId);

        Student student = new Student();
        student.setUser_id(user_id);
        student.setUsername(username);
        student.setEmail(email);
        student.setPhone(phone);
        student.setClass_id(classId);

        return adminMapper.insertUser(student) &&
                adminMapper.userRole(student) &&
                adminMapper.insertUser_info(student);

    }

    public boolean changeStudentInfoDetail(String form) {

        boolean flag1 = true, flag2;

        JSONObject formData = JSON.parseObject(form);
        /*获取id*/
        String user_id = formData.getString("user_id");
        /*获取用户名称*/
        String username = formData.getString("username");
        /*获取用户邮箱*/
        String email = formData.getString("email");
        /*获取用户电话*/
        String phone = formData.getString("phone");
        /*获取专业管理员参与管理的专业*/
        JSONArray jsonArray = formData.getJSONArray("classValue");
        int classId = jsonArray.getInteger(2);

        Student studenttest = adminMapper.getUserByUserID(user_id);

        Student student = new Student();
        student.setUser_id(user_id);
        student.setUsername(username);
        student.setEmail(email);
        student.setPhone(phone);
        student.setClass_id(classId);

        if(studenttest.getEmail() != null){
            if(student.getEmail() != null){
                if(studenttest.getEmail().equals(student.getEmail())){
                    student.setEmail(null);
                }
            }else{
                student.setEmail(null);
            }
        }else{
            flag1 = false;
        }
        if(studenttest.getPhone() != null){
            if(student.getPhone() != null){
                if(studenttest.getPhone().equals(student.getPhone())){
                    student.setPhone(null);
                }
            }else{
                student.setPhone(null);
            }
        }else{
            flag1 = false;
        }

        if(!flag1){
            flag1 = adminMapper.updateUser(student);
        }
        if(studenttest.getClass_id() == student.getClass_id()){
            flag2 = true;
        }else{
            flag2 = adminMapper.updateUser_info(student);
        }


        return flag1 && flag2;

    }


    public boolean deleteStudentInfoDetail(String userID) {

        return adminMapper.deleteStudentInfoDetail(userID);

    }

    public boolean insertTeacherInfoDetail(String form) {

        List<String> ids = rootMapper.getAllID();

        JSONObject formData = JSON.parseObject(form);
        /*获取id*/
        String user_id = formData.getString("user_id");
        if(ids.contains(user_id)){
            return false;
        }
        /*获取用户名称*/
        String username = formData.getString("username");
        /*获取用户邮箱*/
        String email = formData.getString("email");
        /*获取用户电话*/
        String phone = formData.getString("phone");
        /*获取专业管理员参与管理的专业*/
        int majorValue = formData.getInteger("majorValue");


        Teacher teacher = new Teacher();
        teacher.setUser_id(user_id);
        teacher.setUsername(username);
        teacher.setEmail(email);
        teacher.setPhone(phone);
        teacher.setMajor_id(majorValue);

        return adminMapper.insertTUser(teacher) &&
                adminMapper.userTRole(teacher) &&
                adminMapper.insertTUser_info(teacher);

    }

    public boolean changeTeacherInfoDetail(String form) {

        boolean flag1 = true, flag2;

        JSONObject formData = JSON.parseObject(form);
        /*获取id*/
        String user_id = formData.getString("user_id");
        /*获取用户名称*/
        String username = formData.getString("username");
        /*获取用户邮箱*/
        String email = formData.getString("email");
        /*获取用户电话*/
        String phone = formData.getString("phone");
        /*获取专业管理员参与管理的专业*/
        int majorValue = formData.getInteger("majorValue");

        Teacher teachertest = adminMapper.getTeacherByUserID(user_id);

        Teacher teacher = new Teacher();
        teacher.setUser_id(user_id);
        teacher.setUsername(username);
        teacher.setEmail(email);
        teacher.setPhone(phone);
        teacher.setMajor_id(majorValue);

        if(teachertest.getEmail() != null){
            if(teacher.getEmail() != null){
                if(teachertest.getEmail().equals(teacher.getEmail())){
                    teacher.setEmail(null);
                }
            }else{
                teacher.setEmail(null);
            }
        }else{
            flag1 = false;
        }
        if(teachertest.getPhone() != null){
            if(teacher.getPhone() != null){
                if(teachertest.getPhone().equals(teacher.getPhone())){
                    teacher.setPhone(null);
                }
            }else{
                teacher.setPhone(null);
            }
        }else{
            flag1 = false;
        }

        if(!flag1){
            flag1 = adminMapper.updateTUser(teacher);
        }


        if(teachertest.getMajor_id() == teacher.getMajor_id()){
            flag2 = true;
        }else{
            flag2 = adminMapper.updateTUser_info(teacher);
        }


        return flag1 && flag2;

    }

    public boolean deleteTeacherInfoDetail(String userID) {

        return adminMapper.deleteTeacherInfoDetail(userID);

    }

    public List<Major> getAllGrade(String majorList) {

        JSONArray jsonArray = JSON.parseArray(majorList);
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < jsonArray.size(); i++){
            list.add(JSON.parseObject(jsonArray.getString(i)).getInteger("major_id"));
        }

        return adminMapper.getAllGrade(list);

    }

    public boolean updateGrade(String form) {

        JSONObject jsonObject = JSON.parseObject(form);
        List<Grade> gradeList = adminMapper.getGradesById(jsonObject.getInteger("major_id"));
        int id = jsonObject.getInteger("value");
        String label = jsonObject.getString("label");
        for(int i = 0; i < gradeList.size(); i++){
            if(label.equals(gradeList.get(i).getGrade_name())){
                return false;
            }
        }
        return adminMapper.updateGrade(id, label);
    }

    public boolean updateClass(String form) {

        JSONObject jsonObject = JSON.parseObject(form);
        List<Sclass> sclassList = adminMapper.getClassesById(jsonObject.getInteger("grade_id"));
        int id = jsonObject.getInteger("value");
        String label = jsonObject.getString("label");
        for(int i = 0; i < sclassList.size(); i++){
            if(sclassList.get(i).getClass_name().equals(label)) {
                return false;
            }
        }
        return adminMapper.updateClass(id, label);
    }

    public boolean deleteGrade(int grade_id) {
        return adminMapper.deleteGrade(grade_id);
    }

    public boolean deleteClass(int class_id) {
        return adminMapper.deleteClass(class_id);
    }

    public boolean insertGrade(String form) {

        List<Grade> gradeList = adminMapper.getAllGradeWithNone();

        JSONObject jsonObject = JSON.parseObject(form);

        int id = jsonObject.getInteger("id");

        JSONArray jsonArray = jsonObject.getJSONArray("label");

        Set<String> set = new HashSet<>();

        for(int i = 0; i < jsonArray.size(); i++){
            boolean flag = false;
            for(int j = 0; j < gradeList.size(); j++){
                if(gradeList.get(j).getGrade_name().equals(jsonArray.getJSONObject(i).getString("value"))
                        && gradeList.get(j).getMajor_id() == id){
                    flag = true;
                }
            }
            if(flag){
                continue;
            }else {
                set.add(jsonArray.getJSONObject(i).getString("value"));
            }
        }

        if(set.size() == 0){
            return true;
        }else{
            return adminMapper.insertGrade(id, set);
        }

    }

    public List<Major> getMajorAndGrade(String majorList) {

        JSONArray jsonArray = JSON.parseArray(majorList);

        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < jsonArray.size(); i++){
            list.add(JSON.parseObject(jsonArray.getString(i)).getInteger("major_id"));
        }

        return adminMapper.getMajorAndGrade(list);
    }

    public boolean insertClass(String form) {

        List<Sclass> classList = adminMapper.getAllClassWithNone();

        JSONObject jsonObject = JSON.parseObject(form);

        int id = jsonObject.getJSONArray("classValue").getInteger(1);

        JSONArray jsonArray = jsonObject.getJSONArray("label");

        Set<String> set = new HashSet<>();

        for(int i = 0; i < jsonArray.size(); i++){
            boolean flag = false;
            for(int j = 0; j < classList.size(); j++){
                if(classList.get(j).getClass_name().equals(jsonArray.getJSONObject(i).getString("value"))
                   && classList.get(j).getGrade_id() == id){
                    flag = true;
                }
            }
            if(flag){
                continue;
            }else {
                set.add(jsonArray.getJSONObject(i).getString("value"));
            }
        }

        if(set.size() == 0){
            return true;
        }else{
            return adminMapper.insertClass(id, set);
        }

    }

    public boolean importStudents(List<Student> students) {

        Map<String, Integer> map = new HashMap<>();
        for(Student student : students){

            if(map.containsKey(student.getAcademy_name() + student.getMajor_name()+student.getGrade_name()+student.getClass_name())){
                student.setClass_id(map.get(student.getAcademy_name() + student.getMajor_name()+student.getGrade_name()+student.getClass_name()));
            }else{

                String class_id = adminMapper.getClassIDByName(student);
                if(class_id != null && Integer.parseInt(class_id) != 0){
                    student.setClass_id(Integer.parseInt(class_id));
                    map.put(student.getAcademy_name() + student.getMajor_name()+student.getGrade_name()+student.getClass_name(), Integer.parseInt(class_id));
                }else{
                    return false;
                }
            }

        }

        return adminMapper.importStudents(students) && adminMapper.importStudentsInfo(students) && adminMapper.importStudentsRole(students);

    }

    public boolean importTeachers(List<Teacher> teachers) {

        Map<String, Integer> map = new HashMap<>();
        for(Teacher teacher : teachers){

            if(map.containsKey(teacher.getMajor_name())){
                teacher.setMajor_id(map.get(teacher.getMajor_name()));
            }else{

                String major_id = adminMapper.getMajorIDByName(teacher);
                if(major_id != null && Integer.parseInt(major_id) != 0){
                    teacher.setMajor_id(Integer.parseInt(major_id));
                    map.put(teacher.getMajor_name(), Integer.parseInt(major_id));
                }else{
                    return false;
                }
            }

        }

        return adminMapper.importTeachers(teachers) && adminMapper.importTeachersInfo(teachers) && adminMapper.importTeachersRole(teachers);

    }

    public List<Major> getallCourse(String majorList) {

        JSONArray jsonArray = JSON.parseArray(majorList);
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < jsonArray.size(); i++){
            list.add(JSON.parseObject(jsonArray.getString(i)).getInteger("major_id"));
        }

        return adminMapper.getAllCourse(list);

    }

    public boolean updateCourse(String form) {

        JSONObject jsonObject = JSON.parseObject(form);
        List<Course> courseList = adminMapper.getCoursesById(jsonObject.getInteger("major_id"));
        int id = jsonObject.getInteger("value");
        String label = jsonObject.getString("label");
        for(int i = 0; i < courseList.size(); i++){
            if(courseList.get(i).getCourse_name().equals(label)) {
                return false;
            }
        }
        return adminMapper.updateCourse(id, label);

    }

    public boolean deleteCourse(int course_id) {

        return adminMapper.deleteCourse(course_id);

    }

    public boolean insertCourse(String form) {

        List<Course> courseList = adminMapper.getAllCourseWithNone();

        JSONObject jsonObject = JSON.parseObject(form);

        int id = jsonObject.getInteger("id");

        JSONArray jsonArray = jsonObject.getJSONArray("label");

        Set<String> set = new HashSet<>();

        for(int i = 0; i < jsonArray.size(); i++){
            boolean flag = false;
            for(int j = 0; j < courseList.size(); j++){
                if(courseList.get(j).getCourse_name().equals(jsonArray.getJSONObject(i).getString("value"))
                        && courseList.get(j).getMajor_id() == id){
                    flag = true;
                }
            }
            if(flag){
                continue;
            }else {
                set.add(jsonArray.getJSONObject(i).getString("value"));
            }
        }

        if(set.size() == 0){
            return true;
        }else{
            return adminMapper.insertCourse(id, set);
        }

    }

    public Map<String, Object> getClassAndTeacherByMajorlist(String majorList) {

        Map<String, Object> map = new HashMap<>();
        JSONArray jsonArray = JSON.parseArray(majorList);
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < jsonArray.size(); i++){
            list.add(JSON.parseObject(jsonArray.getString(i)).getInteger("major_id"));
        }
        List<Major> majors = adminMapper.getClassByMajorList(list);
        List<Major> teachers = adminMapper.getAllteachers();
        map.put("majors", majors);
        map.put("teachers", teachers);
        return map;
    }

    public boolean submitClassAndTeacher(String form) {

        JSONObject jsonObject = JSON.parseObject(form);

        int course_id = jsonObject.getInteger("course_id");

        List<Integer> classList = new ArrayList<>();

        JSONArray jsonArray = jsonObject.getJSONArray("classValue");
        for(int i = 0; i < jsonArray.size(); i++){
            if(adminMapper.testClassAndCourse(jsonArray.getJSONArray(i).getInteger(2), course_id)){
                continue;
            }
            classList.add(jsonArray.getJSONArray(i).getInteger(2));
        }


        JSONArray jsonArray1 = jsonObject.getJSONArray("teacher_id");

        String teacher_id = jsonArray1.getString(1);

        if(adminMapper.testTeacherAndCourse(teacher_id, course_id)){
            if(classList.size() == 0){
                return true;
            }
            return adminMapper.insertCourseAndClass(classList, course_id);
        }else{
            return adminMapper.insertCourseAndClass(classList, course_id) &&
                    adminMapper.insertCourseAndTeacher(teacher_id, course_id);
        }

    }

    public Map<String, Object> getClassesAndTeacher(int course_id) {

        List<MGCBean> mgcBeans = adminMapper.getClasses(course_id);
        Teacher teacher = adminMapper.getTeacher(course_id);
        Map<String, Object> map = new HashMap<>();
        map.put("mgcBeans", mgcBeans);
        map.put("teacher", teacher);
        return map;

    }

    public boolean deleteCourseAndTeacherAndClassConnect(int course_id) {

        return adminMapper.deleteCourseClassConnect(course_id) && adminMapper.deleteCourseTeacherConnect(course_id);

    }

    public boolean changePassword(String form) {

        JSONObject jsonObject = JSON.parseObject(form);
        String password = jsonObject.getString("password1");
        String user_id = jsonObject.getString("user_id");
        BCryptPasswordEncoder bCryptPasswordEncoder =  new BCryptPasswordEncoder();
        String passwordEncode = bCryptPasswordEncoder.encode(password);
        return adminMapper.changePassword(user_id, passwordEncode);

    }
}
