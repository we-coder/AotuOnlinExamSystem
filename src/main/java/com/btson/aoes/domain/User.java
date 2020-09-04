package com.btson.aoes.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="user")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" }) //防止懒加载
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String username;
    private String nickname;
    private String password;
    private String sex;
    private String address;
    private int age;
    private String status;
    private Date create_date;
    private String email;
    private String tel;
    private Date modify_date;
    private Date login_date;
    private String user_icon;


    @JsonBackReference//标记的属性会被序列化，防止json无限循环
    @ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    private List<OESRole> roles;         //权限

    @JsonBackReference//标记的属性会被序列化，防止json无限循环
    @ManyToMany(mappedBy = "users")
    private List<OESClass> OESClasses;        //班级


    public Date getLogin_date() {
        return login_date;
    }

    public void setLogin_date(Date login_date) {
        this.login_date = login_date;
    }

    public List<OESClass> getOESClasses() {
        return OESClasses;
    }

    public void setOESClasses(List<OESClass> OESClasses) {
        this.OESClasses = OESClasses;
    }

    public List<OESRole> getRoles() {
        return roles;
    }

    public void setRoles(List<OESRole> roles) {
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Date getModify_date() {
        return modify_date;
    }

    public void setModify_date(Date modify_date) {
        this.modify_date = modify_date;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                ", status='" + status + '\'' +
                ", create_date=" + create_date +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", modify_date=" + modify_date +
                ", user_icon='" + user_icon + '\'' +
                ", roles=" + roles +
                ", OESClasses=" + OESClasses +
                '}';
    }
}
