package com.btson.aoes.domain;

import javax.persistence.*;

@Entity
@Table(name="tests")
public class Tests {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String quetion_id;
    private String user_id;
    private String examinee;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuetion_id() {
        return quetion_id;
    }

    public void setQuetion_id(String quetion_id) {
        this.quetion_id = quetion_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getExaminee() {
        return examinee;
    }

    public void setExaminee(String examinee) {
        this.examinee = examinee;
    }
}
