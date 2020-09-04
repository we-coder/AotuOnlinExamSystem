package com.btson.aoes.service;

import com.btson.aoes.domain.UserExam;
import com.btson.aoes.repository.UserExamRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserExamService {
    @Resource
    UserExamRepository userExamRepository;

    public UserExam saveUserExamOne(UserExam userExam){
        return userExamRepository.save(userExam);
    }
    public List<UserExam> saveUserExamAll(List<UserExam> userExamList){
        return userExamRepository.saveAll(userExamList);
    }
    public List<UserExam> findUEByUserId(String id){
        return userExamRepository.findByUser_id(id);
    }
    public UserExam findUserExamOne(int id){
        return userExamRepository.getOne(id);
    }
    public UserExam findUEByUserExam(String user_id,String exam_id){
        return userExamRepository.findUEByUserAndExam(user_id,exam_id);
    }

    public List<UserExam> findUEByExam(int exam_id){
        return userExamRepository.findByExam_id(exam_id);
    }
    public List<UserExam> findUEByEidCid(String exam_id,String class_id){
        return userExamRepository.findByEidAndCid(exam_id,class_id);
    }
}
