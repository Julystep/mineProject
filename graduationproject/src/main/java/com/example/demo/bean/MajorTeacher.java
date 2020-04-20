package com.example.demo.bean;

import java.util.List;

public class MajorTeacher {
    private int value;
    private String label;
    private List<Teacher> children;

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

    public List<Teacher> getChildren() {
        return children;
    }

    public void setChildren(List<Teacher> children) {
        this.children = children;
    }
}
