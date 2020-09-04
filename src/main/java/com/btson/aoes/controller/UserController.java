package com.btson.aoes.controller;

import com.btson.aoes.domain.*;
import com.btson.aoes.service.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private OESRoleService roleService;
    @Resource
    private DistinguishService distinguishService; //文件上传识别模块


    @RequestMapping("/login.action")
    @ResponseBody
    public Map<String,Object> login(User user ,HttpSession session, HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        String username=user.getUsername();
        String password=user.getPassword();
        User user1=userService.getUserByNameAndPwd(username,password);
        if(user1!=null) {
            map.put("msg","OK");
            session.setAttribute("USER_SESSION",user1);

            return map;
        }else{
            map.put("msg","error");
            return map;
        }
    }

    @RequestMapping("/register")
    @ResponseBody
    public Map<String,Object> register(User user){
        Map<String,Object> map=new HashMap<>();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 加密
        String encodedPassword = passwordEncoder.encode(user.getPassword().trim());
        user.setPassword(encodedPassword);

        String username=user.getUsername();
        User user1=userService.getUserByName(username);
        if(user1==null){

            map.put("msg","OK");
            return map;

        }else{
            map.put("msg","该用户名已存在");
            return map;

        }

    }
    @RequestMapping("/manage")
    public String userManage(){
        List<User> users=userService.getAllUser();
        User user2=userService.getUserById(10001);
        User user=new User();
        for (User user1:users){
            if (user1.getId()==10001){
                user=user1;
            }
        }
        user.getRoles();

        return "user/user-manage";
    }

    @RequestMapping("/profile")
    public String userProfile() {

        return "user/profile";
    }


    @RequestMapping("/findUserAll")
    @ResponseBody
    public Map<String,Object>  findUserAll(){
        List<User> users=userService.getAllUser();
        Map<String,Object> map=new HashMap<>();
        int count=users.size();

        map.put("code",0);
        map.put("msg","");
        map.put("count",count);
        map.put("data",users);
        return map;
    }
    @RequestMapping("/getRole")
    @ResponseBody
    public Map<String,Object> getRole(int user_id){
        User user=userService.getUserById(user_id);
        Map<String, Object> map=new HashMap<>();
        map.put("role",user.getRoles());
        return map;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,Object> deleteUserOne(int[] ids){
        Map<String,Object> map=new HashMap<>();
        System.out.println(ids[0]);
        //计算条数
        int count=0;
        //批量删除
        for(int id:ids){
            System.out.println(id);
            userService.deleteUser(id);
            count++;
        }
        map.put("num",count);
        map.put("msg","OK");
        return map;
    }

    @RequestMapping("/update")
    @ResponseBody
    public String updateUser(User user,HttpSession session){
        int id=user.getId();
        User user2=userService.getUserById(id);
        user.setModify_date(new Date());
        user.setUser_icon(user2.getUser_icon());
        user.setPassword(user2.getPassword());
        user.setCreate_date(user2.getCreate_date());
        user.setUsername(user2.getUsername());
        user.setStatus(user2.getStatus());
        user.setOESClasses(user2.getOESClasses());
        user.setRoles(user2.getRoles());
        user.setLogin_date(user2.getLogin_date());
        userService.save(user);

        //重新设置session,仅针对当前登录用户修改自身资料有效
        //当前登录用户id
        int user_id=Integer.parseInt(userService.findUserIdByUsername());
        if (user_id==id){
            session.setAttribute("USER_SESSION",user);
        }

        return "OK";
    }

    @RequestMapping("/alterRole")
    @ResponseBody
    public String alterRole(int role_id,int user_id){
        User user=userService.getUserById(user_id);
        if(role_id==0){
        }else {
            List<OESRole> roles=new ArrayList<>();
            OESRole role=roleService.findRoleById(role_id);
            roles.add(role);
            user.setRoles(roles);
        }
        userService.save(user);
        return "OK";
    }

    @RequestMapping("/add")
    @ResponseBody
    public String addUser(User user){
        userService.save(user);
        return "OK";
    }

    /**
     * 执行头像上传
     */
    @RequestMapping("/fileUpload")
    @ResponseBody
    public String handleFormUpload(@RequestParam("uploadfile") List<MultipartFile> uploadfile,
                                   HttpSession session) throws FileNotFoundException {
        //当前登录用户id
        int user_id=Integer.parseInt(userService.findUserIdByUsername());
        User user=userService.getUserById(user_id);
        user.setId(user_id);

        String diyPath="static/uploadfile/user/icon";//设置上传文件相对路径

        String uploadFileName=distinguishService.uploadFile(uploadfile,diyPath); //上传文件
        //判断文件是否上传成功
        if(uploadFileName=="error"||uploadFileName==null){
            return "error";
        }else{
        user.setUser_icon(uploadFileName);
        user.setModify_date(new Date()); //设置修改日期
        userService.save(user);    //保存
        //利用用户id重新获取用户更新后的数据，重置session（把新数据放入）
        User user2=userService.getUserById(user_id);
        session.setAttribute("USER_SESSION", user2);
            return"OK";
        }
    }

    @RequestMapping("/dataAnalysis")
    public String dataAnalysis(){
        return "analysis";
    }



    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/toRegister")
    public String toRegister(){
        return "register";
    }

    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
