package com.btson.aoes.domain;

import javax.persistence.*;

@Entity
@Table(name = "user_class_test")
public class User_class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int user_id;
    private int class_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    @Override
    public String toString() {
        return "User_class{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", class_id=" + class_id +
                '}';
    }
}
