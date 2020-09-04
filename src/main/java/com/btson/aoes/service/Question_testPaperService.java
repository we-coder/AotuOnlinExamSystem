package com.btson.aoes.service;

import com.btson.aoes.domain.Question_testPaper;
import com.btson.aoes.repository.Question_testPaperRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class Question_testPaperService {
    @Resource
    private Question_testPaperRepository question_testPaperRepository;

    @Transactional
    public void updateQt(Question_testPaper question_testPaper){
        question_testPaperRepository.save(question_testPaper);
    }

    public Question_testPaper findQTId(int qid,int tid){
        return question_testPaperRepository.findByQ_idAndT_id(qid,tid);
    }

    public int findTotalScore(int testPaper_id){
        return question_testPaperRepository.findTotalScore(testPaper_id);
    }
}
