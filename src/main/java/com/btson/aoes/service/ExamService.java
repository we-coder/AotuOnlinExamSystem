package com.btson.aoes.service;

import com.btson.aoes.domain.*;
import com.btson.aoes.repository.ExamRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ExamService {
    @Resource
   private ExamRepository examRepository;
    @Resource
    private OESClassService oesClassService;

    public Exam addExam(Exam exam){
       return examRepository.save(exam);
    }

    //如果考试结束时间超过当前时间，则该考试为已考 exam_status=1
    public void setExamStatus(String teacher_id){
        Date date=new Date();
        List<Exam> examList=examRepository.findExamByUser(teacher_id);
        for(Exam exam:examList){
            if(exam.getEnd_time().before(date)){
                exam.setExam_status(1);
                examRepository.save(exam);
            }

        }

    }

    //查找当前教师的所有试卷信息
    public List<Exam> findExamsByCondition(Exam exam){
        setExamStatus(exam.getTeacher_id());
        Map<String,Object> map=new HashMap<>();
        ExampleMatcher matcher = ExampleMatcher.matching()
                //.withMatcher("question_title", ExampleMatcher.GenericPropertyMatchers.startsWith())//模糊查询匹配开头，即{username}%
                .withMatcher("exam_name" ,ExampleMatcher.GenericPropertyMatchers.contains())//全部模糊查询，即%{address}%
                .withMatcher("teacher_id" ,ExampleMatcher.GenericPropertyMatchers.contains())
                .withIgnorePaths("id","exam_status");  //忽略属性：是否关注。因为是基本类型，需要忽略掉

        Example<Exam> example = Example.of(exam,matcher);
        Pageable pageable = PageRequest.of(0,99999, Sort.Direction.DESC, "id");
        Page<Exam> exams=examRepository.findAll(example,pageable);
        List<Exam> examList=new ArrayList<>();
        for (Exam e:exams){
            String cid=e.getClass_id();
            if (e.getClass_id()==null||e.getClass_id().equals("")){
                cid="99";
            }
            OESClass oesClass=oesClassService.findOneClass(Integer.parseInt(cid));
            e.setClass_id(oesClass.getClass_name());
            examList.add(e);
        }
        return examList;
    }
    public Exam findExamOne(int id){
        return examRepository.getOne(id);
    }

    //显示考试管理信息
    public void examShow(List<Exam> examList,Model model){
        Date date=new Date();
        List<Exam> examListNew=new ArrayList<>();
        List<Exam> examListOld=new ArrayList<>();
        for(Exam exam:examList){
            if(exam.getExam_status()==0){
                examListNew.add(exam);
            }else{
                examListOld.add(exam);
            }
        }
        model.addAttribute("examListNew",examListNew);
        model.addAttribute("examListOld",examListOld);
        model.addAttribute("examList",examList);
    }

    //查找当前登录考生所有的考试信息
    public List<Exam> findExamineesExamAll(List<Integer> ids){
        List<Exam> exams =examRepository.findAllById(ids);
        List<Exam> examList=new ArrayList<>();
        for (Exam e:exams){
            String cid=e.getClass_id();
            if (e.getClass_id()==null||e.getClass_id().equals("")){
                cid="99";
            }
            OESClass oesClass=oesClassService.findOneClass(Integer.parseInt(cid));
            e.setClass_id(oesClass.getClass_name());
            examList.add(e);
        }
        return examList;
    }
}
