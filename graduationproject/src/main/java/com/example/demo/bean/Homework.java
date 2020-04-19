package com.example.demo.bean;

import com.alibaba.fastjson.JSON;

import java.util.Date;
import java.util.List;

public class Homework {

    private int id;
    private String title;
    private String main;
    private Date sdate;
    private Date edate;
    private String teacher_id;
    private int state;
    private String judge;
    /*主要服务于学生查询自己成绩*/
    private String comment;
    private boolean iscomment;
    private List<Major> children;
    private List<MGCBean> children1;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isIscomment() {
        return iscomment;
    }

    public void setIscomment(boolean iscomment) {
        this.iscomment = iscomment;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public List<MGCBean> getChildren1() {
        return children1;
    }

    public void setChildren1(List<MGCBean> children1) {
        this.children1 = children1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public Date getSdate() {
        return sdate;
    }

    public void setSdate(Date sdate) {
        this.sdate = sdate;
    }

    public Date getEdate() {
        return edate;
    }

    public void setEdate(Date edate) {
        this.edate = edate;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<Major> getChildren() {
        return children;
    }

    public void setChildren(List<Major> children) {
        this.children = children;
    }
}
