package com.btson.aoes.domain;

import javax.persistence.*;

@Entity
@Table(name = "userexam")
public class UserExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String exam_id;
    private String user_id;
    private int class_id;
    private int status;
    private String duration;    //持续时间

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserExam{" +
                "id=" + id +
                ", exam_id='" + exam_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", class_id=" + class_id +
                ", status=" + status +
                ", duration='" + duration + '\'' +
                '}';
    }
}
