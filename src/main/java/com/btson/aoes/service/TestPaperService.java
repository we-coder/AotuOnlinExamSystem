package com.btson.aoes.service;

import com.btson.aoes.domain.Catalogue;
import com.btson.aoes.domain.Question;
import com.btson.aoes.domain.Question_testPaper;
import com.btson.aoes.domain.TestPaper;
import com.btson.aoes.repository.TestPaperRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TestPaperService {
    @Resource
    private TestPaperRepository testPaperRepository;
    @Resource
    private Question_testPaperService questionTestPaperService;
    @Resource
    private CatalogueService catalogueService;

    @Transactional
    public TestPaper testPaperAdd(TestPaper testPaper){

        return testPaperRepository.save(testPaper);

    }
    public TestPaper findTestPaperById(int id){
        TestPaper testPaper=testPaperRepository.findById(id);
//        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date s_time=testPaper.getStart_time();
//        Date e_time=testPaper.getEnd_time();
//        System.out.println(time.format(s_time));
//        testPaper.setStart_time(Timestamp.valueOf(time.format(s_time)));
        return testPaper;
    }
    public List<TestPaper> findTestPaperAll(){
        return testPaperRepository.findAll();
    }

    //获取最新试卷id
    public TestPaper findLastTestPaper(String id){
        return testPaperRepository.findLastTestPaper(id);
    }

    //搜索试卷
    public List<TestPaper> findTestPaperByCondition(TestPaper testPaper){
        ExampleMatcher matcher = ExampleMatcher.matching()
                //.withMatcher("question_title", ExampleMatcher.GenericPropertyMatchers.startsWith())//模糊查询匹配开头，即{username}%
                .withMatcher("type" ,ExampleMatcher.GenericPropertyMatchers.contains())//全部模糊查询，即%{address}%
                .withMatcher("name" ,ExampleMatcher.GenericPropertyMatchers.contains())//全部模糊查询，即%{address}%
                .withMatcher("user_id",ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id");  //忽略属性：是否关注。因为是基本类型，需要忽略掉
        Pageable pageable = PageRequest.of(0,9999999, Sort.Direction.DESC, "id");
        Example<TestPaper> example = Example.of(testPaper,matcher);
        Page<TestPaper> testPapers=testPaperRepository.findAll(example,pageable);
        List<TestPaper> testPaperList=new ArrayList<>();
        for (TestPaper t:testPapers){
            String cid=t.getType();
            if (t.getType()==null||t.getType().equals("")){
                cid="99";
            }
            Catalogue catalogue=catalogueService.findOne(Integer.parseInt(cid));
            t.setType(catalogue.getTitle());
            testPaperList.add(t);
        }
        return testPaperList;
    }

    public void showTestPaper(int id, Model model){
        TestPaper testPaper=findTestPaperById(id);
        List<Question> questions=testPaper.getQuestions();
        List<Question> question1=new ArrayList<>();
        List<Question> question2=new ArrayList<>();
        List<Question> question3=new ArrayList<>();
        List<Question> question4=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        for (Question question:questions){
            Question_testPaper question_testPaper=questionTestPaperService.findQTId(question.getId(),testPaper.getId());
            question.setQuestion_subject(String.valueOf(question_testPaper.getScore()));//分数
            String type=question.getQuestion_type();
            if(type.contains("单选")){
                question1.add(question);
            }else if(type.contains("多选")){
                question2.add(question);
            }else if(type.contains("判断")){
                question3.add(question);
            }else if(type.contains("主观")){
                question4.add(question);
            }
        }
        //1单选，2多选，3判断，4主观
        model.addAttribute("testPaper",testPaper);
        model.addAttribute("question1",question1);
        model.addAttribute("question2",question2);
        model.addAttribute("question3",question3);
        model.addAttribute("question4",question4);

    }
}

