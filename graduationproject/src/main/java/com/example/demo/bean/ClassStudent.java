package com.example.demo.bean;

public class ClassStudent {

    private String student_id;
    private int homework_id;
    private String homework_name;
    private String student_name;
    private String url;
    private String comment;
    private int sclass_id;
    private String class_name;
    private String grade_name;
    private String major_name;
    private boolean state;
    private boolean iscomment;
    private String judge;
    private String homework_main;

    public String getHomework_main() {
        return homework_main;
    }

    public void setHomework_main(String homework_main) {
        this.homework_main = homework_main;
    }

    public String getHomework_name() {
        return homework_name;
    }

    public void setHomework_name(String homework_name) {
        this.homework_name = homework_name;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isIscomment() {
        return iscomment;
    }

    public void setIscomment(boolean iscomment) {
        this.iscomment = iscomment;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public int getHomework_id() {
        return homework_id;
    }

    public void setHomework_id(int homework_id) {
        this.homework_id = homework_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getSclass_id() {
        return sclass_id;
    }

    public void setSclass_id(int sclass_id) {
        this.sclass_id = sclass_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }

    public String getMajor_name() {
        return major_name;
    }

    public void setMajor_name(String major_name) {
        this.major_name = major_name;
    }


    @Override
    public String toString() {
        return "ClassStudent{" +
                "student_id='" + student_id + '\'' +
                ", homework_id=" + homework_id +
                ", homework_name='" + homework_name + '\'' +
                ", student_name='" + student_name + '\'' +
                ", url='" + url + '\'' +
                ", comment='" + comment + '\'' +
                ", sclass_id=" + sclass_id +
                ", class_name='" + class_name + '\'' +
                ", grade_name='" + grade_name + '\'' +
                ", major_name='" + major_name + '\'' +
                ", state=" + state +
                ", iscomment=" + iscomment +
                ", judge='" + judge + '\'' +
                ", homework_main='" + homework_main + '\'' +
                '}';
    }
}
