package com.example.demo.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Academy {

    private int value;
    private String label;
    private List<Major> children;
    private int academy_id;
    private String academy_name;

    public int getAcademy_id() {
        return academy_id;
    }

    public void setAcademy_id(int academy_id) {
        this.academy_id = academy_id;
    }

    public String getAcademy_name() {
        return academy_name;
    }

    public void setAcademy_name(String academy_name) {
        this.academy_name = academy_name;
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

    public List<Major> getChildren() {
        return children;
    }

    public void setChildren(List<Major> children) {
        this.children = children;  
    }
}
