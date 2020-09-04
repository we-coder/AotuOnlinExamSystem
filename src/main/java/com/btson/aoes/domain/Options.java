package com.btson.aoes.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name="options")
public class Options {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer opt_id;
    private String opt_content;
    private int opt_status;     //状态，1：正确，0：错误

    @JsonBackReference//标记的属性会被序列化，防止json无限循环
    @ManyToOne(targetEntity=Question.class,cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)//可选属性optional=false,表示author不能为空。删除答案，不影响题目
    @JoinColumn(name="opt_qt_id",referencedColumnName="id")//设置在option表中的关联字段(外键)
    private Question question;//所属题目

    public Integer getOpt_id() {
        return opt_id;
    }

    public void setOpt_id(Integer opt_id) {
        this.opt_id = opt_id;
    }

    public String getOpt_content() {
        return opt_content;
    }

    public void setOpt_content(String opt_content) {
        this.opt_content = opt_content;
    }

    public int getOpt_status() {
        return opt_status;
    }

    public void setOpt_status(int opt_status) {
        this.opt_status = opt_status;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Options{" +
                "opt_id=" + opt_id +
                ", opt_content='" + opt_content + '\'' +
                ", opt_status=" + opt_status +
                ", question=" + question +
                '}';
    }
}
