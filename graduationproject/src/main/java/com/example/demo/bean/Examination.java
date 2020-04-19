package com.example.demo.bean;

import java.util.Date;
import java.util.List;

public class Examination {
    private int id;
    private String examination_title;
    private String examination_main;
    /*考试的可以开始的日期和结束的日期*/
    private Date sdate;
    private Date edate;
    /*考试的时限*/
    private String timeLimit;
    /*考试的状态*/
    private int state;
    /*学生所选问题和回答*/
    private String answer;
    private String teacher_id;
    private String comment;
    private String iscomment;
    private List<Major> children;
    private List<MGCBean> children1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExamination_title() {
        return examination_title;
    }

    public void setExamination_title(String examination_title) {
        this.examination_title = examination_title;
    }

    public String getExamination_main() {
        return examination_main;
    }

    public void setExamination_main(String examination_main) {
        this.examination_main = examination_main;
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

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIscomment() {
        return iscomment;
    }

    public void setIscomment(String iscomment) {
        this.iscomment = iscomment;
    }

    public List<Major> getChildren() {
        return children;
    }

    public void setChildren(List<Major> children) {
        this.children = children;
    }

    public List<MGCBean> getChildren1() {
        return children1;
    }

    public void setChildren1(List<MGCBean> children1) {
        this.children1 = children1;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
