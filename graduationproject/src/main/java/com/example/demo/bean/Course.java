package com.example.demo.bean;

public class Course {

    private int course_id;
    private String course_name;
    private String course_avator;
    private String course_picture;
    private int major_id;
    private String value;
    private String label;


    public String getCourse_picture() {
        return course_picture;
    }

    public void setCourse_picture(String course_picture) {
        this.course_picture = course_picture;
    }

    public int getMajor_id() {
        return major_id;
    }

    public void setMajor_id(int major_id) {
        this.major_id = major_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_avator() {
        return course_avator;
    }

    public void setCourse_avator(String course_avator) {
        this.course_avator = course_avator;
    }
}
