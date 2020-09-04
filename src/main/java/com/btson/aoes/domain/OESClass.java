package com.btson.aoes.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "oes_class")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class OESClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String class_name;
    private String class_teacher;

    @JsonBackReference//标记的属性会被序列化，防止json无限循环
    @ManyToMany
    @JoinTable(name = "user_class",joinColumns =@JoinColumn(name = "class_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;           //考生

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_teacher() {
        return class_teacher;
    }

    public void setClass_teacher(String class_teacher) {
        this.class_teacher = class_teacher;
    }
}
