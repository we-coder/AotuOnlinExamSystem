package com.btson.aoes.service;

import com.btson.aoes.domain.AnswerSheet;
import com.btson.aoes.repository.AnswerSheetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AnswerSheetService {
    @Resource
    AnswerSheetRepository answerSheetRepository;
    @Transactional
    public AnswerSheet saveAS(AnswerSheet answerSheet){
        return answerSheetRepository.save(answerSheet);
    }
    @Transactional
    public List<AnswerSheet> saveASAll(List<AnswerSheet> answerSheetList){
        return answerSheetRepository.saveAll(answerSheetList);
    }
    public List<AnswerSheet> findListUE(int user_id,int exam_id){
        return answerSheetRepository.findAllByCS(user_id,exam_id);
    }
    public int[] findExamIds(){
        return answerSheetRepository.findExamIds();
    }
    public List<AnswerSheet> findAllExam(int exam_id){
        return answerSheetRepository.findAllByExam(exam_id);
    }
    public int[] findAllTeacher(int cs,int teacher_id){
        return answerSheetRepository.findAllByTeacher(cs,teacher_id);
    }
    public List<AnswerSheet> findAllUE(int user_id,int exam_id,int cs){
        return answerSheetRepository.findAllByUE(user_id,exam_id,cs);
    }
    public AnswerSheet findOneOUE(int opt_id,int user_id,int exam_id){
        return answerSheetRepository.findOneByOUE(opt_id,user_id,exam_id);
    }

    public List<Object> findCorrectionData(int cs,int teacher_id){
        return answerSheetRepository.findCorrectionData(cs,teacher_id);
    }
}
