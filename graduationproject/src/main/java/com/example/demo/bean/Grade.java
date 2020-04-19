package com.example.demo.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Grade {

    private int value;  /*对应id*/
    private String label;  /*对应名称*/
    private int major_id;
    private List<Sclass> children;
    private int id;
    private String grade_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
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

    public int getMajor_id() {
        return major_id;
    }

    public void setMajor_id(int major_id) {
        this.major_id = major_id;
    }

    public List<Sclass> getChildren() {
        return children;
    }

    public void setChildren(List<Sclass> children) {
        this.children = children;
    }
}
