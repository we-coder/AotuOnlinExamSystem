package com.btson.aoes.controller;

import com.btson.aoes.domain.Catalogue;
import com.btson.aoes.domain.User;
import com.btson.aoes.service.CatalogueService;
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
@RequestMapping("/catalogue")
public class CatalogueController {
    @Resource
    private CatalogueService catalogueService;
    @Resource
    private UserService userService;

    @RequestMapping("/getId")
    @ResponseBody
    public Map<String,Object> addCatalogue(int ids){
        //当前登录用户id
        int user_id=Integer.parseInt(userService.findUserIdByUsername());
        Map<String,Object> map=new HashMap<>();
        Catalogue catalogue=new Catalogue();
        catalogue.setTitle("未命名");
        Catalogue catalogue1=catalogueService.addCatalogue(catalogue,ids,user_id);
        int id=catalogue1.getId();
        map.put("id",id);
        map.put("msg","OK");
        return map;
    }

    @RequestMapping("/getAllData")
    @ResponseBody
    public Map<String,Object> getAllData(){
        Map<String,Object> map=new HashMap<>();
        //当前登录用户id
        int user_id=Integer.parseInt(userService.findUserIdByUsername());
        List<Catalogue> catalogues=catalogueService.findAllCatalogue(user_id);
        map.put("catalogue",catalogues);
        map.put("msg","OK");
        return  map;
    }

    @RequestMapping("/update")
    @ResponseBody
    public Map<String,Object> updateCatalogue(Catalogue catalogue){
        Map<String,Object> map=new HashMap<>();
        catalogueService.updateCatalogueTitle(catalogue.getTitle(),catalogue.getId());
        map.put("msg","OK");
        return  map;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,Object> deleteCatalogue(int id){
        Map<String,Object> map=new HashMap<>();
        catalogueService.deleteCatalogue(id);
        map.put("msg","OK");
        return  map;
    }
}
