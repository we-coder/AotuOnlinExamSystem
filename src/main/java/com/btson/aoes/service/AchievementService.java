package com.btson.aoes.service;

import com.btson.aoes.domain.OESAchievement;
import com.btson.aoes.repository.AchievementRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AchievementService {
    @Resource
    AchievementRepository achievementRepository;

    public List<OESAchievement> saveAchievement(List<OESAchievement> achievements) {
        return achievementRepository.saveAll(achievements);
    }
    public OESAchievement findAchievementByUE(int user_id,int exam_id,int question_id){
        return achievementRepository.findAchievementByUE(user_id,exam_id,question_id);
    }
    public List<OESAchievement> findAcByUE(int user_id,int exam_id){
        return achievementRepository.findAcByUE(user_id,exam_id);
    }

    public List<OESAchievement> findAcByExam(int exam_id){
        return achievementRepository.findAcByExam_id(exam_id);
    }
}
