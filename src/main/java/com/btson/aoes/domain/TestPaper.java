package com.btson.aoes.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="testpaper")
public class TestPaper {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String name;     //试卷名
    private String type;      //试卷分类
    private String user_id;   //创建人
    private String duration;    //持续时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date start_time;   //开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end_time;     //结束时间

    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date create_time;   //创建时间

    @JsonBackReference//标记的属性会被序列化，防止json无限循环
    @ManyToMany
    @JoinTable(name = "question_testpaper",joinColumns = @JoinColumn(name = "testpaper_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private List<Question> questions ;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TestPaper{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", user_id='" + user_id + '\'' +
                ", duration='" + duration + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", create_time=" + create_time +
                ", questions=" + questions +
                '}';
    }
}
