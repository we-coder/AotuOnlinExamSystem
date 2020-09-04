package com.btson.aoes.controller;

import com.btson.aoes.domain.Notice;
import com.btson.aoes.domain.OESRole;
import com.btson.aoes.domain.User;
import com.btson.aoes.service.CatalogueService;
import com.btson.aoes.service.NoticeService;
import com.btson.aoes.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class IndexController {
    @Resource
    UserService userService;
    @Resource
    NoticeService noticeService;
    @Resource
    private CatalogueService catalogueService;

    @RequestMapping("/")
    public String index(){
        System.out.println("主页...");
        return "index";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("user", getUsername());
        model.addAttribute("role", getAuthority());
        return "home";
    }

    @RequestMapping("/register")
    @ResponseBody
    public String register(User user,int role_id,Model model){
        if(user.getPassword()==null){
            return "register";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 加密
        String encodedPassword = passwordEncoder.encode(user.getPassword().trim());
        user.setPassword(encodedPassword);
        user.setCreate_date(new Date());
        user.setUser_icon("100050_48d2185c-e2da-4d13-9467-0a0b7b63d85d_sys.jpg");
        //统一赋予用户权限
        List<OESRole> roles=new ArrayList<>();
        OESRole role=new OESRole();
        role.setId(role_id);
        roles.add(role);
        user.setRoles(roles);
        String username=user.getUsername();
        User user1=userService.getUserByName(username);
        if(user1==null){
            userService.save(user);
            userService.save(user);
            User user2=userService.getUserByName(username);
            catalogueService.saveCatalogue(user2.getId());
            return "OK";

        }else{
            model.addAttribute("msg","该用户名已存在");
            return "error";

        }

    }

    @RequestMapping(value = "/admin")
    public String adminPage(Model model, HttpSession session,HttpServletRequest request) {
        String url=setUserInfo(model,session,request);
        return "redirect:/admin/dataAnalysis";
    }

    @RequestMapping(value = "/teacher")
    public String teacherPage(Model model, HttpSession session,HttpServletRequest request) {
        String url=setUserInfo(model,session,request);
        return "redirect:/question/manage";
    }

    @RequestMapping(value = "/stu")
    public String stuPage(Model model, HttpSession session,HttpServletRequest request) {
        String url=setUserInfo(model,session,request);
        return "redirect:/examinees/manage";
    }

    @RequestMapping(value = "/accessDenied")
    public String accessDeniedPage(Model model) {
        model.addAttribute("user", getUsername());
        model.addAttribute("role", getAuthority());
        return "accessDenied";
    }


    @RequestMapping(value="/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        // Authentication是一个接口，表示用户认证信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // 如果用户认知信息不为空，注销
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        // 重定向到login页面
        return "redirect:/login?logout";
    }

    /**
     * 用户信息设置
     */
    private String setUserInfo(Model model,HttpSession session,HttpServletRequest request){
        model.addAttribute("user", getUsername());
        model.addAttribute("role", getAuthority());
        User user=userService.getUserByName(getUsername());
        user.setLogin_date(new Date());
        userService.save(user);
        session.setAttribute("USER_SESSION", user);
        List<Notice> notices=noticeService.findNoticeByRS(String.valueOf(user.getId()),0);
        session.setAttribute("NOTICE_SESSION",notices);
        return null;
    }

    /**
     * 获得当前用户名称
     * */
    private String getUsername(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("username = " + username);
        return username;
    }

    /**
     * 获得当前用户权限
     * */
    private String getAuthority(){
        // 获得Authentication对象，表示用户认证信息。
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = new ArrayList<String>();
        // 将角色名称添加到List集合
        for (GrantedAuthority a : authentication.getAuthorities()) {
            roles.add(a.getAuthority());
        }
        System.out.println("role = " + roles);
        return roles.toString();
    }

}
