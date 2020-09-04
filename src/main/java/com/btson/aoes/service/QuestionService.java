package com.btson.aoes.service;

import com.btson.aoes.domain.Catalogue;
import com.btson.aoes.domain.Options;
import com.btson.aoes.domain.Question;
import com.btson.aoes.repository.QuestionRepository;
import com.btson.aoes.tools.CurrentTimer;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


@Service
public class QuestionService {
    @Resource
    private QuestionRepository questionRepository;
    @Resource
    private UserService userService;
    @Resource
    private CatalogueService catalogueService;

    @Transactional
    public Question questionAdd(Question question,String[] opt_content,int[] opt_status,int[] opt_id) {

        // 选项处理;
        List<Options> options=new ArrayList<Options>();
        if(opt_content!=null){
            int count=0;
            for(String content:opt_content){
                Options option=new Options();
                if(opt_id!=null){
                    option.setOpt_id(opt_id[count]);
                }
                option.setQuestion(question);
                option.setOpt_content(content);
                option.setOpt_status(opt_status[count]);
                options.add(option);
                count++;
            }
        }
        question.setOptions(options);

        //当前登录用户id
        int user_id=Integer.parseInt(userService.findUserIdByUsername());
        String userId=String.valueOf(user_id);
        question.setUser_id(userId);
        // 存入mysql中的时间格式“yyyy/MM/dd HH:mm:ss”间
        Date nowTime = CurrentTimer.getCurrentTime();
        question.setCreate_time(nowTime);
        //保存数据
        questionRepository.save(question);
        //返回保存对象
        return questionRepository.findLastQuestion(question.getUser_id());

    }



    //分页查询
    @Transactional
    public Page<Question> findQuestionByPage(Question question,Integer pageNumber,Integer pageSize){

        Pageable pageable =PageRequest.of(pageNumber,pageSize,Sort.Direction.ASC, "id");
        return questionRepository.findAll(pageable);
    }

    //条件查询
    public Map<String,Object> findQuestionByCondition(Question question, Integer pageNumber, Integer pageSize){
        Map<String,Object> map=new HashMap<>();
        ExampleMatcher matcher = ExampleMatcher.matching()
                //.withMatcher("question_title", ExampleMatcher.GenericPropertyMatchers.startsWith())//模糊查询匹配开头，即{username}%
                .withMatcher("question_title" ,ExampleMatcher.GenericPropertyMatchers.contains())//全部模糊查询，即%{address}%
                .withIgnorePaths("id");  //忽略属性：是否关注。因为是基本类型，需要忽略掉
        Example<Question> example = Example.of(question,matcher);
        Pageable pageable =PageRequest.of(pageNumber,pageSize,Sort.Direction.DESC, "id");
        Page<Question> questions=questionRepository.findAll(example,pageable);
        List<Question> questionList=new ArrayList<>();
        for (Question question1:questions){
            String cid=question1.getQuestion_class();
            if (question1.getQuestion_class()==null||question1.getQuestion_class().equals("")){
                cid="99";
            }
            Catalogue catalogue=catalogueService.findOne(Integer.parseInt(cid));
            question1.setQuestion_class(catalogue.getTitle());
            questionList.add(question1);
        }
        long count=questions.getTotalElements();
        map.put("count",count);
        map.put("data",questionList);
        //处理数据
        map.put("code",0);
        map.put("msg","");
        return map;
    }

    public  List<Question> findQuestionsRandom(Question question,int num){
        //当前登录用户id
        int user_id=Integer.parseInt(userService.findUserIdByUsername());
        String question_class=question.getQuestion_class();
        String question_type=question.getQuestion_type();
        String question_level=question.getQuestion_level();
        return questionRepository.findByRandom(question_class,question_level,question_type,num,user_id);
    }

    public Question findQuestionOne(int id){
        return questionRepository.findById(id);
    }

    public void deleteQuestion(int id){
        questionRepository.deleteById(id);
    }

    public void saveAllQuestions(List<Question> questions){
        questionRepository.saveAll(questions);
    }

    private List<Question> findUploadQuestions(int user_id,String source){
        return questionRepository.findUploadQuestions(user_id,source);
    }

}
