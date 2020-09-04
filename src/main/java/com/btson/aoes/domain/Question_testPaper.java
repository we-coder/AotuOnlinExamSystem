package com.btson.aoes.domain;

import javax.annotation.Generated;
import javax.persistence.*;

@Entity
@Table(name = "question_testpaper")
public class Question_testPaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int score;
    private int question_id;
    private int testpaper_id;

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

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getTestpaper_id() {
        return testpaper_id;
    }

    public void setTestpaper_id(int testpaper_id) {
        this.testpaper_id = testpaper_id;
    }
}
