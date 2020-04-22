package com.example.demo.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bean.ClassStudent;
import com.example.demo.bean.ClassStudentOnlineTest;
import com.example.demo.bean.Student;
import com.example.demo.bean.Teacher;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PoiUtils {

    public static ResponseEntity<byte[]> exportStudentTestExcel(List<ClassStudentOnlineTest> classStudents, String studentJudge) {

        HttpHeaders headers = null;
        ByteArrayOutputStream baos = null;
        try{
            //创建excel文档
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            //创建摘要
            hssfWorkbook.createInformationProperties();
            //获取文档信息，并配置
            DocumentSummaryInformation documentSummaryInformation = hssfWorkbook.getDocumentSummaryInformation();
            //文档类别
            documentSummaryInformation.setCategory("学生考试信息");
            //文档组织结构
            documentSummaryInformation.setCompany("沈阳工业大学");
            //获取摘要配置信息
            SummaryInformation summaryInformation = hssfWorkbook.getSummaryInformation();
            //设置文档主题
            summaryInformation.setSubject("学生考试信息表");
            //设置文档标题
            summaryInformation.setTitle("学生考试信息");
            //设置文档坐着
            summaryInformation.setAuthor("沈阳工业大学");
            //设置文档备注
            summaryInformation.setComments("待会备注班级");
            //创建excel表单
            HSSFSheet sheet = hssfWorkbook.createSheet("沈阳工业大学考试成绩表单");
            //创建日期显示格式
            HSSFCellStyle dateCellStyle = hssfWorkbook.createCellStyle();
            dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
            //创建标题显示样式
            HSSFCellStyle headerStyle = hssfWorkbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            /*创建如果不存在评分项会出现的东西*/
            HSSFCellStyle otherStyle = hssfWorkbook.createCellStyle();
            otherStyle.setFillForegroundColor(IndexedColors.BLUE.index);
            otherStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            /*创建如果没提交作业会出现的东西*/
            HSSFCellStyle wrongStyle = hssfWorkbook.createCellStyle();
            wrongStyle.setFillForegroundColor(IndexedColors.RED.index);
            wrongStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //定义列的宽度
            sheet.setColumnWidth(0, 15 * 256);
            sheet.setColumnWidth(1, 15 * 256);
            sheet.setColumnWidth(2, 30 * 256);
            sheet.setColumnWidth(3, 15 * 256);
            sheet.setColumnWidth(4, 20 * 256);
            sheet.setColumnWidth(5, 20 * 256);
            sheet.setColumnWidth(6, 15 * 256);
            sheet.setColumnWidth(7, 20 * 256);
            //设置表头
            HSSFRow headerRow = sheet.createRow(0);
            HSSFCell cell1 = headerRow.createCell(0);
            cell1.setCellValue("学号");
            cell1.setCellStyle(headerStyle);
            HSSFCell cell2 = headerRow.createCell(1);
            cell2.setCellValue("姓名");
            cell2.setCellStyle(headerStyle);
            HSSFCell cell3 = headerRow.createCell(2);
            cell3.setCellValue("班级");
            cell3.setCellStyle(headerStyle);
            HSSFCell cell4 = headerRow.createCell(3);
            cell4.setCellValue("考试题目名称");
            cell4.setCellStyle(headerStyle);
            HSSFCell cell5 = headerRow.createCell(4);
            cell5.setCellValue("总分");
            cell5.setCellStyle(headerStyle);
            HSSFCell []cell6 = new HSSFCell[5];
            for(int i = 0; i < 5; i++){
                cell6[i] = headerRow.createCell(i+5);
                cell6[i].setCellValue("评分项" + i);
                cell6[i].setCellStyle(headerStyle);
            }
            //格式化日期
            String formatDate = null;
            //装数据
            int allScores = 0;
            for(int i = 0; i < classStudents.size(); i++){
                HSSFRow row = sheet.createRow(i + 1);
                ClassStudentOnlineTest classStudent = classStudents.get(i);
                if(JSON.parseObject(classStudent.getAnswer()) == null){
                    row.createCell(0).setCellValue(classStudent.getStudent_id());
                    row.createCell(1).setCellValue(classStudent.getStudent_name());
                    row.createCell(2).setCellValue(classStudent.getMajor_name() + classStudent.getGrade_name() + classStudent.getClass_name());
                    row.createCell(3).setCellValue("该生未参加考试");
                    row.getCell(3).setCellStyle(wrongStyle);
                }else{
                    JSONObject question = JSON.parseObject(classStudent.getAnswer()).getJSONObject("question");
                    JSONArray judge = JSON.parseObject(classStudent.getAnswer()).getJSONArray("judge");
                    allScores = 0;
                    row.createCell(0).setCellValue(classStudent.getStudent_id());
                    row.createCell(1).setCellValue(classStudent.getStudent_name());
                    row.createCell(2).setCellValue(classStudent.getMajor_name() + classStudent.getGrade_name() + classStudent.getClass_name());
                    row.createCell(3).setCellValue(question.getString("title"));

                    if(judge.size() == 0){
                        row.createCell(4).setCellValue("未提交");
                        for(int j = 0; j < 5; j++){
                            row.createCell(j + 5).setCellValue(0);
                        }
                    }else{
                        if(judge.size() != 0){
                            for(int j = 0; j < judge.size(); j++){
                                allScores += Integer.parseInt(judge.getJSONObject(j).getString("value"));
                            }
                            row.createCell(4).setCellValue(allScores);
                            for(int j = 0; j < 5; j++){
                                if(judge.size() >= j + 1){
                                    row.createCell(j + 5).setCellValue(judge.getJSONObject(j).getString("name") + ":" + judge.getJSONObject(j).getString("value"));
                                }else{
                                    row.createCell(j + 5).setCellValue("本题不存在该评分项");
                                    row.getCell(j+5).setCellStyle(otherStyle);
                                }
                            }
                        }

                    }
                }
            }
            headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", new String("学生考试成绩信息表.xls".getBytes("UTF-8"), "iso-8859-1"));
            baos = new ByteArrayOutputStream();
            hssfWorkbook.write(baos);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);

    }

    public static ResponseEntity<byte[]> exportEmp2Excel(List<ClassStudent> classStudents, String homeworkJudge) throws Exception{

        JSONArray jsonArray = JSON.parseArray(homeworkJudge);
        HttpHeaders headers = null;
        ByteArrayOutputStream baos = null;
        try{
            //创建excel文档
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            //创建摘要
            hssfWorkbook.createInformationProperties();
            //获取文档信息，并配置
            DocumentSummaryInformation documentSummaryInformation = hssfWorkbook.getDocumentSummaryInformation();
            //文档类别
            documentSummaryInformation.setCategory("学生作业信息");
            //文档组织结构
            documentSummaryInformation.setCompany("沈阳工业大学");
            //获取摘要配置信息
            SummaryInformation summaryInformation = hssfWorkbook.getSummaryInformation();
            //设置文档主题
            summaryInformation.setSubject("学生作业信息表");
            //设置文档标题
            summaryInformation.setTitle("学生作业信息");
            //设置文档坐着
            summaryInformation.setAuthor("沈阳工业大学");
            //设置文档备注
            summaryInformation.setComments("待会备注班级");
            //创建excel表单
            HSSFSheet sheet = hssfWorkbook.createSheet("沈阳工业大学作业成绩表单");
            //创建日期显示格式
            HSSFCellStyle dateCellStyle = hssfWorkbook.createCellStyle();
            dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
            //创建标题显示样式
            HSSFCellStyle headerStyle = hssfWorkbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //定义列的宽度
            sheet.setColumnWidth(0, 15 * 256);
            sheet.setColumnWidth(1, 15 * 256);
            sheet.setColumnWidth(2, 30 * 256);
            sheet.setColumnWidth(3, 15 * 256);
            sheet.setColumnWidth(4, 20 * 256);
            sheet.setColumnWidth(5, 20 * 256);
            sheet.setColumnWidth(6, 15 * 256);
            sheet.setColumnWidth(7, 20 * 256);
            //设置表头
            HSSFRow headerRow = sheet.createRow(0);
            HSSFCell cell1 = headerRow.createCell(0);
            cell1.setCellValue("学号");
            cell1.setCellStyle(headerStyle);
            HSSFCell cell2 = headerRow.createCell(1);
            cell2.setCellValue("姓名");
            cell2.setCellStyle(headerStyle);
            HSSFCell cell3 = headerRow.createCell(2);
            cell3.setCellValue("班级");
            cell3.setCellStyle(headerStyle);
            HSSFCell cell4 = headerRow.createCell(3);
            cell4.setCellValue("作业名称");
            cell4.setCellStyle(headerStyle);
            HSSFCell cell5 = headerRow.createCell(4);
            cell5.setCellValue("总分");
            cell5.setCellStyle(headerStyle);
            HSSFCell []cell6 = new HSSFCell[jsonArray.size()];
            for(int i = 0; i < jsonArray.size(); i++){
                cell6[i] = headerRow.createCell(i+5);
                cell6[i].setCellValue(jsonArray.getJSONObject(i).getString("name"));
                cell6[i].setCellStyle(headerStyle);
            }
            //格式化日期
            String formatDate = null;
            //装数据
            int allScores = 0;
            for(int i = 0; i < classStudents.size(); i++){
                HSSFRow row = sheet.createRow(i + 1);
                ClassStudent classStudent = classStudents.get(i);
                allScores = 0;
                row.createCell(0).setCellValue(classStudent.getStudent_id());
                row.createCell(1).setCellValue(classStudent.getStudent_name());
                row.createCell(2).setCellValue(classStudent.getMajor_name() + classStudent.getGrade_name() + classStudent.getClass_name());
                row.createCell(3).setCellValue(classStudent.getHomework_name());

                if(classStudent.getJudge() == null || "".equals(classStudent.getJudge())){
                    row.createCell(4).setCellValue("未提交");
                    for(int j = 0; j < jsonArray.size(); j++){

                        row.createCell(j + 5).setCellValue(0);
                    }
                }else{
                    JSONArray jsonArray1 = JSON.parseArray(classStudent.getJudge());
                    if(jsonArray1.size() != 0){
                        for(int j = 0; j < jsonArray1.size(); j++){
                            allScores += Integer.parseInt(jsonArray1.getJSONObject(j).getString("value"));
                        }
                        row.createCell(4).setCellValue(allScores);
                        for(int j = 0; j < jsonArray1.size(); j++){
                            row.createCell(j + 5).setCellValue(jsonArray1.getJSONObject(j).getString("value"));
                        }
                    }
                    if(jsonArray1.size() == 0){

                        row.createCell(4).setCellValue("未提交");
                        for(int j = 0; j < jsonArray1.size(); j++){
                            row.createCell(j + 5).setCellValue(0);
                        }
                    }
                }
            }
            headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", new String("学生作业成绩信息表.xls".getBytes("UTF-8"), "iso-8859-1"));
            baos = new ByteArrayOutputStream();
            hssfWorkbook.write(baos);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    public static List<Student> importStudents(MultipartFile file) {

        List<Student> students = new ArrayList<>();
        try{

            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            int numberOfSheets = workbook.getNumberOfSheets();
            for(int i = 0; i < numberOfSheets; i++){
                HSSFSheet sheet = workbook.getSheetAt(i);
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                Student student;
                for(int j = 0; j < physicalNumberOfRows; j++){
                    if(j == 0){
                        continue; //标题行
                    }
                    HSSFRow row = sheet.getRow(j);
                    if(row == null){
                        continue; //没数据
                    }
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    student = new Student();
                    for(int k = 0; k < physicalNumberOfCells; k++){

                        HSSFCell cell = row.getCell(k);
                        switch (cell.getCellTypeEnum()) {
                            case STRING: {
                                String cellValue = cell.getStringCellValue();
                                if (cellValue == null) {
                                    cellValue = "";
                                }
                                switch (k) {
                                    case 0:
                                        student.setUser_id(cellValue);
                                        break;
                                    case 1:
                                        student.setUsername(cellValue);
                                        break;
                                    case 2:
                                        student.setEmail(cellValue);
                                        break;
                                    case 3:
                                        student.setPhone(cellValue);
                                        break;
                                    case 4:
                                        student.setClass_name(cellValue);
                                        break;
                                    case 5:
                                        student.setGrade_name(cellValue);
                                        break;
                                    case 6:
                                        student.setMajor_name(cellValue);
                                        break;
                                    case 7:
                                        student.setAcademy_name(cellValue);
                                }
                            }
                            break;
                        }
                    }
                    students.add(student);
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }

        return students;

    }

    public static List<Teacher> importTeachers(MultipartFile file) {

        List<Teacher> teachers = new ArrayList<>();
        try{

            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            int numberOfSheets = workbook.getNumberOfSheets();
            for(int i = 0; i < numberOfSheets; i++){
                HSSFSheet sheet = workbook.getSheetAt(i);
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                Teacher teacher;
                for(int j = 0; j < physicalNumberOfRows; j++){
                    if(j == 0){
                        continue; //标题行
                    }
                    HSSFRow row = sheet.getRow(j);
                    if(row == null){
                        continue; //没数据
                    }
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    teacher = new Teacher();
                    for(int k = 0; k < physicalNumberOfCells; k++){

                        HSSFCell cell = row.getCell(k);
                        switch (cell.getCellTypeEnum()) {
                            case STRING: {
                                String cellValue = cell.getStringCellValue();
                                if (cellValue == null) {
                                    cellValue = "";
                                }
                                switch (k) {
                                    case 0:
                                        teacher.setUser_id(cellValue);
                                        break;
                                    case 1:
                                        teacher.setUsername(cellValue);
                                        break;
                                    case 2:
                                        teacher.setEmail(cellValue);
                                        break;
                                    case 3:
                                        teacher.setPhone(cellValue);
                                        break;
                                    case 4:
                                        teacher.setMajor_name(cellValue);
                                        break;
                                }
                            }
                            break;
                        }
                    }
                    teachers.add(teacher);
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }

        return teachers;

    }
}
