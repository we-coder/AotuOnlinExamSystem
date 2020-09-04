package com.btson.aoes.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="question")
public class Question {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String question_content;      //题目内容
    private String question_title;       //题目名称
    @Temporal(TemporalType.TIMESTAMP) // 是用来定义日期类型
    private Date create_time;    //创建时间
    private String user_id;             //创建人
    private String question_type;        //试题类型：单选、多选、判断、填空、问答
    private String question_subject;     //分类2
    private String question_class;       //分类3
    private String question_describe;   //描述
    private String question_analysis;   //解析
    private String question_level;     //难度
    private String answer;
    private String source;        //试题来源



    @OneToMany(mappedBy = "question",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    //@OrderBy("opt_id desc")
    //级联保存、更新、删除、刷新;延迟加载。当删除题目，会级联删除该题目的所有选项
    //拥有mappedBy注解的实体类为关系被维护端
    //mappedBy="question"中的question是Option中的question属性
    private List<Options> options;

    @JsonBackReference//标记的属性会被序列化，防止json无限循环
    @ManyToMany(mappedBy = "questions")
    private List<TestPaper> testPapers;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    public List<TestPaper> getTestPapers() {
        return testPapers;
    }

    public void setTestPapers(List<TestPaper> testPapers) {
        this.testPapers = testPapers;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public String getQuestion_title() {
        return question_title;
    }

    public void setQuestion_title(String question_title) {
        this.question_title = question_title;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public String getQuestion_subject() {
        return question_subject;
    }

    public void setQuestion_subject(String question_subject) {
        this.question_subject = question_subject;
    }

    public String getQuestion_class() {
        return question_class;
    }

    public void setQuestion_class(String question_class) {
        this.question_class = question_class;
    }

    public String getQuestion_describe() {
        return question_describe;
    }

    public void setQuestion_describe(String question_describe) {
        this.question_describe = question_describe;
    }

    public String getQuestion_analysis() {
        return question_analysis;
    }

    public void setQuestion_analysis(String question_analysis) {
        this.question_analysis = question_analysis;
    }

    public String getQuestion_level() {
        return question_level;
    }

    public void setQuestion_level(String question_level) {
        this.question_level = question_level;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }

}
