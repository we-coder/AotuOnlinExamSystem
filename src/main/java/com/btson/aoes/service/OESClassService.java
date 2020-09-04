package com.btson.aoes.service;

import com.btson.aoes.domain.OESClass;
import com.btson.aoes.domain.TestPaper;
import com.btson.aoes.domain.User;
import com.btson.aoes.repository.OESClassRepository;
import org.junit.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

@Service
public class OESClassService {
    @Resource
    private OESClassRepository oesClassRepository;

    @Transactional
    public List<OESClass> findAllClass(){
        return oesClassRepository.findAll();
    }

    public OESClass findOneClass(int id){
        return oesClassRepository.getOne(id);
    }

    @Transactional
    public OESClass saveClass(OESClass oesClass){
        return oesClassRepository.save(oesClass);
    }

    public void classUpdate(OESClass oesClass,int[] user_ids){

        OESClass classSaved=findOneClass(oesClass.getId());
        List<User> userSave=classSaved.getUsers();
        List<User> users=new ArrayList<>();

        //存储旧数据

        for(User user:userSave){
            User userOld=new User();
            userOld.setId(user.getId());
            users.add(userOld);

        }

        //新数据
        for(int user_id:user_ids){
            User userNew=new User();
            userNew.setId(user_id);
            users.add(userNew);

            for(User user:userSave){
                if(user_id==user.getId()){   //判断是否重复考生
                    users.remove(userNew);
                    break;
                }
            }


        }
      oesClass.setUsers(users);
        List<OESClass> oesClassList=new ArrayList<>();
        oesClassList.add(oesClass);
        oesClassList.add(classSaved);
        oesClassRepository.saveAll(oesClassList);
    }

    @Transactional
    public void deleteUserByUc(int user_id,int class_id){
        oesClassRepository.deleteUserOne(user_id, class_id);
    }

    public List<OESClass> findByCondition(OESClass oesClass){
        ExampleMatcher matcher = ExampleMatcher.matching()
                //.withMatcher("question_title", ExampleMatcher.GenericPropertyMatchers.startsWith())//模糊查询匹配开头，即{username}%
                .withMatcher("class_name" ,ExampleMatcher.GenericPropertyMatchers.contains())//全部模糊查询，即%{address}%
                .withMatcher("class_teacher" ,ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id");  //忽略属性：是否关注。因为是基本类型，需要忽略掉
        Example<OESClass> oesClassExample = Example.of(oesClass,matcher);
        return oesClassRepository.findAll(oesClassExample);
    }



}
