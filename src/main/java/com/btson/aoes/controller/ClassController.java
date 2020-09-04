package com.btson.aoes.controller;

import com.btson.aoes.domain.OESClass;
import com.btson.aoes.domain.User;
import com.btson.aoes.domain.User_class;
import com.btson.aoes.service.OESClassService;
import com.btson.aoes.service.UserService;
import com.btson.aoes.tools.QRCodeGenerator;
import com.google.zxing.WriterException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/class")
public class ClassController {
    @Resource
    private OESClassService oesClassService;
    @Resource
    private UserService userService;

    @RequestMapping("/manage")
    public String classManage(OESClass oesClass,Model model) {
        oesClass.setClass_teacher(userService.findUserIdByUsername());
        //查询判断
        if(oesClass.getClass_name()==""){
            oesClass.setClass_name(null);
        }

        List<OESClass> OESClasses = oesClassService.findByCondition(oesClass);
        model.addAttribute("class_name",oesClass.getClass_name());
        model.addAttribute("classes", OESClasses);
        return "class/class-manage";

    }

    @RequestMapping("/add")
    @ResponseBody
    public Map<String,Object> addClass(OESClass oesClass,int[] user_ids){

        Map<String,Object> map=new HashMap<>();
        //模拟登录
        oesClass.setClass_teacher(userService.findUserIdByUsername());
        System.out.println("班级id:"+oesClass.getId());
        //判断班级是否存在
        if(oesClass.getId()==0){
            oesClass=oesClassService.saveClass(oesClass); //新建班级
        }else {
            oesClassService.classUpdate(oesClass, user_ids); //修改班级
        }
        map.put("msg","OK");
        map.put("class_id",oesClass.getId());
        return map;
    }
    @RequestMapping("/edit")
    public String editClass(int id,Model model){
        OESClass oesClass=oesClassService.findOneClass(id);
        model.addAttribute("class",oesClass);
        return "class/class-edit";
    }

    //更新修改班级
    @RequestMapping("/update")
    @ResponseBody
    public String updateClass(OESClass oesClass,int[] user_ids){
        OESClass oesClass1=oesClassService.saveClass(oesClass);
//        //循环录入
//        for (int user_id:user_ids){
//            User_class userClass=new User_class();
//            //判断是否存在该考生，如果存在则执行update方法，否则执行insert方法（jpa的save方法有update和insert）
////            User_class checkUC=userClassService.findUCByUC_id(oesClass.getId(),user_id);
////            if(checkUC!=null){
////                userClass.setId(checkUC.getId());
////            }
//            int j=userClassService.addUserClass(oesClass.getId(),user_id);
//        }

        return "OK";

    }

    @RequestMapping("/findAll")
    @ResponseBody
    public Map<String,Object> findClassAll(int id){
        Map<String,Object> map=new HashMap<>();
        Map<String,Object> map1=new HashMap<>();
        OESClass oesClass=oesClassService.findOneClass(id);
        int count=oesClass.getUsers().size();
        map1.put("content",oesClass.getUsers());
        map.put("data",map1);
        map.put("code",0);
        map.put("count",count);
        map.put("msg","");
        return map;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String deleteClass(int class_id,int[] ids){
        int count=0;
        OESClass oesClass=oesClassService.findOneClass(class_id);
        for(int user_id:ids){
            User user=new User();
            user.setId(user_id);
            oesClassService.deleteUserByUc(user_id,class_id);
        }
        return "OK";

    }
    @RequestMapping(value="/qrimage")
    @ResponseBody
    public ResponseEntity<byte[]> getQRImage(int class_id,HttpServletRequest request) {

        String url=request.getServerName();
        int serverPort = request.getServerPort();
        //获取当前班级号
        //二维码内的信息
        String info ="http://"+url+":"+serverPort+"/examinees/toAddClass?class_id="+class_id;
        System.out.println(info);

        byte[] qrcode = null;
        try {
            qrcode = QRCodeGenerator.getQRCodeImage(info, 400, 400);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }

        // Set headers
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<byte[]> (qrcode, headers, HttpStatus.CREATED);
    }
}
