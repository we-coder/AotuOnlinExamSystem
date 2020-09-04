package com.btson.aoes.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "achievement")
public class OESAchievement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int question_id;
    private int score;   //批改分数
    private int status;  //显示状态;0:不显示（默认），1：显示
    private String user_id;  //考生
    private String teacher_id;  //批改老师
    private String exam_id;  //考试场次
    private Date create_time;     //创建时间


    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    @Override
    public String toString() {
        return "OESAchievement{" +
                "id=" + id +
                ", question_id=" + question_id +
                ", score=" + score +
                ", status=" + status +
                ", user_id='" + user_id + '\'' +
                ", exam_id='" + exam_id + '\'' +
                ", create_time=" + create_time +
                '}';
    }
}
