package com.example.demo.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bean.*;
import com.example.demo.common.PoiUtils;
import com.example.demo.common.UserUtils;
import com.example.demo.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {


    @Value("${file.rootPath}")
    private String ROOT_PATH;
    //图片存放根目录下的子目录
    @Value("${file.sonPath2}")
    private String SON_PATH;

    @Autowired
    AdminService adminService;

    @RequestMapping(value = "/getmajorid", method = RequestMethod.GET)
    public Admin getMajorID(@RequestParam("userID") String userID){

        return adminService.getMajorID(userID);

    }

    @RequestMapping(value = "/getstudentbymajor", method = RequestMethod.POST)
    public Map<String, Object> getStudentByMajor(@RequestParam("majorList") String majorList,
                                                 @RequestParam("currentPage") int page,
                                                 @RequestParam("size") int size,
                                                 @RequestParam("form") String form){

        return adminService.getStudentByMajor(majorList, page, size, form);

    }

    @RequestMapping(value = "/getteacherbymajor", method = RequestMethod.POST)
    public Map<String, Object> getTeacherByMajor(@RequestParam("majorList") String majorList,
                                                 @RequestParam("currentPage") int page,
                                                 @RequestParam("size") int size,
                                                 @RequestParam("form") String form){

        return adminService.getTeacherByMajor(majorList, page, size, form);

    }

    @RequestMapping(value = "/getclassesbymajorIid", method = RequestMethod.GET)
    public List<Major> getClassesByMajorId(@RequestParam("majorID") int majorID){

        return adminService.getClassesByMajorId(majorID);

    }

    @RequestMapping(value = "/insertstudentinfo", method = RequestMethod.POST)
    public RespBean insertStudentInfoDetail(@RequestParam("form") String form){

        if(adminService.insertStudentInfoDetail(form)){
            return RespBean.ok("插入学生信息成功");
        }else{
            return RespBean.error("插入学生信息失败");
        }

    }

    @RequestMapping(value = "/changestudentinfo", method = RequestMethod.POST)
    public RespBean changeStudentInfoDetail(@RequestParam("form") String form){

        if(adminService.changeStudentInfoDetail(form)){
            return RespBean.ok("修改学生信息成功");
        }else{
            return RespBean.error("修改学生信息失败");
        }

    }

    @RequestMapping(value = "/deleteStudentInfo", method = RequestMethod.POST)
    public RespBean deleteStudentInfoDetail(@RequestParam("user_id") String userID){

        if(adminService.deleteStudentInfoDetail(userID)){
            return RespBean.ok("删除学生信息成功");
        }else{
            return RespBean.error("删除学生信息失败");
        }

    }

    @RequestMapping(value = "/insertteacherinfo", method = RequestMethod.POST)
    public RespBean insertTeacherInfoDetail(@RequestParam("form") String form){

        if(adminService.insertTeacherInfoDetail(form)){
            return RespBean.ok("插入教师信息成功");
        }else{
            return RespBean.error("插入教师信息失败");
        }

    }

    @RequestMapping(value = "/changeteacherinfo", method = RequestMethod.POST)
    public RespBean changeTeacherInfoDetail(@RequestParam("form") String form){

        if(adminService.changeTeacherInfoDetail(form)){
            return RespBean.ok("修改教师信息成功");
        }else{
            return RespBean.error("修改教师信息失败");
        }

    }

    @RequestMapping(value = "/deleteTeacherInfo", method = RequestMethod.POST)
    public RespBean deleteTeacherInfoDetail(@RequestParam("user_id") String userID){

        if(adminService.deleteTeacherInfoDetail(userID)){
            return RespBean.ok("删除教师信息成功");
        }else{
            return RespBean.error("删除教师信息失败");
        }

    }

    @RequestMapping(value = "/getallgrade", method = RequestMethod.POST)
    public List<Major> getAllMajor(@RequestParam("majorList") String majorList){

        return adminService.getAllGrade(majorList);

    }

    @RequestMapping(value = "/updategrade", method = RequestMethod.POST)
    public RespBean updateGrade(@RequestParam("form") String form){

        if(adminService.updateGrade(form)){
            return RespBean.ok("更新班级名称成功");
        }else{
            return RespBean.error("更新班级名称失败");
        }

    }

    @RequestMapping(value = "/updateclass", method = RequestMethod.POST)
    public RespBean updateClass(@RequestParam("form") String form){

        if(adminService.updateClass(form)){
            return RespBean.ok("更新班级名称成功");
        }else{
            return RespBean.error("更新班级名称失败");
        }

    }

    @RequestMapping(value = "/deletegrade", method = RequestMethod.POST)
    public RespBean deleteGrade(@RequestParam("grade_id") int grade_id){

        boolean flag = adminService.deleteGrade(grade_id);

        if(flag){
            return RespBean.ok("删除年级成功");
        }else{
            return RespBean.error("删除年级失败");
        }

    }

    @RequestMapping(value = "/deleteclass", method = RequestMethod.POST)
    public RespBean deleteClass(@RequestParam("class_id") int class_id){

        boolean flag = adminService.deleteClass(class_id);

        if(flag){
            return RespBean.ok("删除班级成功");
        }else{
            return RespBean.error("删除班级失败");
        }

    }

    @RequestMapping(value = "/insertgrade", method = RequestMethod.POST)
    public RespBean insertGrade(@RequestParam("form") String form){

        boolean flag = adminService.insertGrade(form);

        if(flag){
            return RespBean.ok("添加年级成功");
        }else{
            return RespBean.error("添加年级失败");
        }

    }

    @RequestMapping(value = "/insertclass", method = RequestMethod.POST)
    public RespBean insertClass(@RequestParam("form") String form){

        boolean flag = adminService.insertClass(form);

        if(flag){
            return RespBean.ok("添加班级成功");
        }else{
            return RespBean.error("添加班级失败");
        }

    }

    @RequestMapping(value = "/getmajorandgrade", method = RequestMethod.POST)
    public List<Major> getMajorAndGrade(@RequestParam("majorList") String majorList){
        return adminService.getMajorAndGrade(majorList);
    }

    @RequestMapping(value = "/importstudents", method = RequestMethod.POST)
    public RespBean importStudents(MultipartFile file){
        List<Student> students = PoiUtils.importStudents(file);

        boolean flag = adminService.importStudents(students);

        if(flag){
            return RespBean.ok("导入学生成功");
        }else{
            return RespBean.error("导入学生失败, 可能原因为数据库不存在该学生填写的班级或学号不和规范");
        }
    }

    @RequestMapping(value = "/importteachers", method = RequestMethod.POST)
    public RespBean importTeachers(MultipartFile file){
        List<Teacher> teachers = PoiUtils.importTeachers(file);

        boolean flag = adminService.importTeachers(teachers);

        if(flag){
            return RespBean.ok("导入教师成功");
        }else{
            return RespBean.error("导入教师失败, 可能原因为数据库不存在该学生填写的班级或学号不和规范");
        }
    }

    @RequestMapping(value = "/getallcourse", method = RequestMethod.POST)
    public List<Major> getAllCourse(@RequestParam("majorList") String majorList){
        return adminService.getallCourse(majorList);
    }

    @RequestMapping(value = "/updatecourse", method = RequestMethod.POST)
    public RespBean updateCourse(@RequestParam("form") String form){

        if(adminService.updateCourse(form)){
            return RespBean.ok("更新课程名称成功");
        }else{
            return RespBean.error("更新课程名称失败");
        }

    }

    @RequestMapping(value = "/deletecourse", method = RequestMethod.POST)
    public RespBean deleteCourse(@RequestParam("course_id") int course_id){

        boolean flag = adminService.deleteCourse(course_id);

        if(flag){
            return RespBean.ok("删除课程成功");
        }else{
            return RespBean.error("删除课程失败");
        }

    }

    @RequestMapping(value = "/insertcourse", method = RequestMethod.POST)
    public RespBean insertCourse(@RequestParam("form") String form){

        boolean flag = adminService.insertCourse(form);

        if(flag){
            return RespBean.ok("添加课程成功");
        }else{
            return RespBean.error("添加课程失败");
        }

    }

    @RequestMapping(value = "/getclassandteacherbymajorlist", method = RequestMethod.POST)
    public Map<String, Object> getClassAndTeacherByMajorlist(@RequestParam("majorList") String majorList){

        return adminService.getClassAndTeacherByMajorlist(majorList);

    }

    @RequestMapping(value = "/submitclassandteacher", method = RequestMethod.POST)
    public RespBean submitClassAndTeacher(@RequestParam("form") String form){

        if(adminService.submitClassAndTeacher(form)){
            return RespBean.ok("成功");
        }else{
            return RespBean.error("失败");
        }

    }

    @RequestMapping(value = "/getclassesandteacher", method = RequestMethod.GET)
    public Map<String, Object> getClassesAndTeacher(@RequestParam("course_id") int course_id){
        return adminService.getClassesAndTeacher(course_id);
    }

    @RequestMapping(value = "/deletecourseandteacherandclassconnect", method = RequestMethod.POST)
    public RespBean deleteCourseAndTeacherAndClassConnect(@RequestParam("course_id") int course_id){

        if(adminService.deleteCourseAndTeacherAndClassConnect(course_id)) {
            return RespBean.ok("删除关联成功");
        }else{
            return RespBean.error("删除关联失败");
        }

    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    public RespBean changePassword(@RequestParam("form") String form){

        if(adminService.changePassword(form)) {
            return RespBean.ok("修改成功");
        }else{
            return RespBean.error("修改失败");
        }

    }

    @RequestMapping(value = "/uploadPicture", method = RequestMethod.POST)
    public RespBean userfaceUpload(int id, String name, MultipartFile file) throws IOException {
        if(file.isEmpty()){
            return RespBean.error("文件为空");
        }
        /*File fileDir = new File(ROOT_PATH + SON_PATH);
        if(!fileDir.exists()){
            fileDir.mkdir();
        }*/

        String fileName = file.getOriginalFilename();
        String suffixName  = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID() + suffixName;   //新文件名
        String majorDir = id + name;
        File dest = new File(ROOT_PATH + SON_PATH + majorDir + '/'+  fileName);
        System.out.println("================================================" + dest.getParentFile());
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        /*String[] children = dest.getParentFile().list();
        for(String child : children){
            File file1 = new File(dest.getParentFile() + child);
            file1.delete();
        }*/
        deleteDirectory(dest.getParentFile());
        file.transferTo(dest);

        String path = SON_PATH  + majorDir + '/' + fileName;
        boolean result = adminService.changeCoursePictureById(id, path);
        /*File toFile = null;
        InputStream ins = null;
        ins = file.getInputStream();
        toFile = new File(file.getOriginalFilename());
        inputStreamToFile(ins, toFile);
        ins.close();
        File file1 = new File(ROOT_PATH + SON_PATH + "\\" + file.getOriginalFilename());
        FileInputStream is = new FileInputStream(toFile);
        FileChannel ifile = is.getChannel();
        FileOutputStream os = new FileOutputStream(file1);
        FileChannel ofile = os.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (ifile.read(buffer) != -1) {//将物品从库存读入到缓冲器（大卡车）
            buffer.flip();
            ofile.write(buffer);//将缓冲器（大卡车）的物品，写出到目的地。
            buffer.clear();

        }*/
        if(result) {
            return RespBean.ok("头像上传成功");
        }else{
            return RespBean.error("头像上传失败");
        }
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
