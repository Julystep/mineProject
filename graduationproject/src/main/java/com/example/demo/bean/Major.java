package com.example.demo.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Major {

    private int value;
    private String label;
    private int acamedy_id;
    private List<Grade> children;
    private int major_id;
    private String major_name;
    private List<Course> childrencourse;

    public List<Course> getChildrencourse() {
        return childrencourse;
    }

    public void setChildrencourse(List<Course> childrencourse) {
        this.childrencourse = childrencourse;
    }

    public int getMajor_id() {
        return major_id;
    }

    public void setMajor_id(int major_id) {
        this.major_id = major_id;
    }

    public String getMajor_name() {
        return major_name;
    }

    public void setMajor_name(String major_name) {
        this.major_name = major_name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @JsonIgnore
    public int getAcamedy_id() {
        return acamedy_id;
    }

    public void setAcamedy_id(int acamedy_id) {
        this.acamedy_id = acamedy_id;
    }

    public List<Grade> getChildren() {
        return children;
    }

    public void setChildren(List<Grade> children) {
        this.children = children;
    }
}
