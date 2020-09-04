package com.btson.aoes.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "exam")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String exam_name;
    private String teacher_id;   //出卷人/老师
    private String class_id;  //考生班级
    private String testpaper_id;  //试卷
    private int exam_status;//考试状态
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date start_time;   //开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end_time;     //结束时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;   //创建时间
    private String duration;    //持续时间

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getExam_status() {
        return exam_status;
    }

    public void setExam_status(int exam_status) {
        this.exam_status = exam_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getTestpaper_id() {
        return testpaper_id;
    }

    public void setTestpaper_id(String testpaper_id) {
        this.testpaper_id = testpaper_id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", exam_name='" + exam_name + '\'' +
                ", teacher_id='" + teacher_id + '\'' +
                ", class_id='" + class_id + '\'' +
                ", testpaper_id='" + testpaper_id + '\'' +
                ", exam_status=" + exam_status +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", create_time=" + create_time +
                ", duration='" + duration + '\'' +
                '}';
    }
}
