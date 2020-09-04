package com.btson.aoes.controller;

import com.btson.aoes.domain.User;
import com.btson.aoes.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminCotroller {
    @Resource
    private UserService userService;
    /*
     * 数据分析
     * */
    @RequestMapping("/dataAnalysis")
    public String adminAnalysis(Model model){
        List<User> userList=userService.getAllUser();
        int admin=0,teacher=0,student=0;
        int userNum=userList.size();//总人数
        for (User user:userList){
            if (user.getRoles().size()==0){ //统计用户级别人数

            }else {
                if (user.getRoles().get(0).getId() == 1){
                    admin++;
                }else if (user.getRoles().get(0).getId() == 2){
                    teacher++;
                }else {
                    student++;
                }
            }
        }
        model.addAttribute("userNum",userNum);
        model.addAttribute("admin",admin);
        model.addAttribute("teacher",teacher);
        model.addAttribute("student",student);
        return "admin/dataAnalysis";
    }

    @RequestMapping("/admin/getData")
    @ResponseBody
    public Map<String,Object> getAnalysisData(){
        Map<String,Object> map=new HashMap<>();
        return map;
    }

}
