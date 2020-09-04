package com.btson.aoes.service;

import com.btson.aoes.domain.Score;
import com.btson.aoes.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ScoreService {
    @Resource
    private ScoreRepository scoreRepository;

    //学生成绩查询
    public List<Score> findScoreAll(int user_id){
        return scoreRepository.findScoreAll(user_id);
    }

    //老师成绩查询
    public List<Score> findScoreByTeacher(int teacher_id){
        return scoreRepository.findScoreAllTeacher(teacher_id);
    }
    public int[] findExamByTeacher(int teacher_id){
        return scoreRepository.findExamInScoreByTeacher(teacher_id);
    }
    public Score findScoreByUE(int user_id,int exam_id){
        return scoreRepository.findScoreByUE(user_id,exam_id);
    }

    //单场考试
    public List<Score> findScoreByET(int exam_id,int teacher_id){
        return scoreRepository.findScoreByET(exam_id,teacher_id);
    }
}
