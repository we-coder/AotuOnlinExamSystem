package com.btson.aoes.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "v_score") //对应视图名称
public class Score {
    /*
    * 此实体查询的是视图，但是视图中没有设置主键，在查询语句时，只能使用返回语句的行号作为主键
    * 所以rowid为行号，作为主键，它的作用是防止JPA因为没有主键而造成查询数据重复
    * */
    @Id
    @Column(name = "rowid")
    private long rowid;
    @Column(name = "grade")
    private String grade;
    @Column(name = "exam_id")
    private String exam_id;
    @Column(name = "user_id")
    private String user_id;
    @Column(name = "create_time")
    private Date create_time;
    @Column(name = "teacher_id")
    private String teacher_id;
    private String exam_name;

    public long getRowid() {
        return rowid;
    }

    public void setRowid(long rowid) {
        this.rowid = rowid;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
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

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }
}