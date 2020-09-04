package com.btson.aoes.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class AnswerSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String option_id;
    private int opt_status;
    private String answer;
    private String user_id;
    private Date create_time;
    private String exam_id;
    private int correction_status; //批改状态，已批，未批
    private String teacher_id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public int getOpt_status() {
        return opt_status;
    }

    public void setOpt_status(int opt_status) {
        this.opt_status = opt_status;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public int getCorrection_status() {
        return correction_status;
    }

    public void setCorrection_status(int correction_status) {
        this.correction_status = correction_status;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }
}
