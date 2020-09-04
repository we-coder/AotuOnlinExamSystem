package com.btson.aoes.controller;

import com.btson.aoes.domain.Catalogue;
import com.btson.aoes.domain.Question;
import com.btson.aoes.domain.Question_testPaper;
import com.btson.aoes.domain.TestPaper;
import com.btson.aoes.service.*;
import com.btson.aoes.tools.CurrentTimer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/testPaper")
public class TestPaperController {
    @Resource
    private TestPaperService testPaperService;
    @Resource
    private QuestionService questionService;
    @Resource
    private CatalogueService catalogueService;
    @Resource
    private Question_testPaperService question_testPaperService;
    @Resource
    private UserService userService;

    @RequestMapping("/manage")
    public String testManage(TestPaper testPaper,String catalogue_title,Model model){
        //当前登录用户id
        testPaper.setUser_id(userService.findUserIdByUsername());
        System.out.println(testPaper.getType());
        //查询判断
        if(testPaper.getType()==""){
            testPaper.setType(null);
        }
        if(testPaper.getName()==""){
            testPaper.setName(null);
        }
        List<TestPaper> testPapers=testPaperService.findTestPaperByCondition(testPaper);
        model.addAttribute("testPaper_type",testPaper.getType());
        model.addAttribute("testPaper_name",testPaper.getName());
        model.addAttribute("catalogue_title",catalogue_title);
        model.addAttribute("testPapers",testPapers);
        return "testPaper/testPaper-manage";
    }
    @RequestMapping("/show")
    public String testShow(int id,Model model){

        testPaperService.showTestPaper(id,model);

        return "testPaper/testPaper";
    }

    //创建试卷
    @RequestMapping("/add")
    @ResponseBody
    public Map<String,Object> addTestPaper(TestPaper testPaper,int[] questions_ids,int[] score){
        Map<String,Object> map=new HashMap<>();
        // 存入mysql中的时间格式“yyyy/MM/dd HH:mm:ss”间
        Date currentTime = CurrentTimer.getCurrentTime();

        testPaper.setCreate_time(currentTime);
        //用户id
        String user_id=userService.findUserIdByUsername();
        //中间表，设置分数
        testPaper.setUser_id(user_id);
        int i=0;
        testPaper=testPaperService.testPaperAdd(testPaper);
        //建立试卷，设置分数
        for(int question_id:questions_ids){
            //获取中间的id
            Question_testPaper question_testPaper=new Question_testPaper();
            Question_testPaper checkQT=question_testPaperService.findQTId(question_id,testPaper.getId());
                    if(checkQT!=null){
                        question_testPaper.setId(checkQT.getId());
                    }
            question_testPaper.setQuestion_id(question_id);
            question_testPaper.setTestpaper_id(testPaper.getId());
            question_testPaper.setScore(score[i]);
            question_testPaperService.updateQt(question_testPaper);
            i++;
        }
        map.put("msg","OK");
        return map;
    }

    //自动抽取
    @RequestMapping(value = "/addExtract",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> addAutoTestPaper(@RequestBody List<Object> objects,String QuestionClass){
        //获取到JSONObjec
        List<Question> questions=new ArrayList<>();
        for(Object object :objects) {
            Question question=new Question();
            Map entry = (Map)object;
            question.setQuestion_type("%"+entry.get("questionType").toString()+"%");
            question.setQuestion_class("%"+QuestionClass+"%");
            String[] levels={"simple","medium","difficult"};
            for (String level:levels) {
                if (entry.get(level).toString() != null || !entry.get(level).toString().equals("0")) {
                    String num=entry.get(level).toString();
                    List<Question> question1 = new ArrayList<>();
                    if(level.equals("simple")){
                        question.setQuestion_level("简单");
                    }else if (level.equals("medium")){
                        question.setQuestion_level("中等");
                    }else if (level.equals("difficult")){
                        question.setQuestion_level("困难");
                    }

                    question1 = questionService.findQuestionsRandom(question, Integer.parseInt(num));
                    questions.addAll(question1);
                }
            }
        }
        System.out.println("QuestionClass:" + questions);

        Map<String,Object> map=new HashMap<>();
        map.put("msg","OK");
        map.put("data",questions);
        return map;
    }

//    试卷更新api
    @RequestMapping("/update")
    @ResponseBody
    public Map<String,Object> updateTestPaper(TestPaper testPaper){
        Map<String,Object> map=new HashMap<>();
        TestPaper testPaper1=testPaperService.findTestPaperById(14);
        map.put("test",testPaper1);
        return map;
    }


    //试卷修改页面
    @RequestMapping("/edit")
    public String editTestPaper(int id ,Model model){
        TestPaper testPaper=testPaperService.findTestPaperById(id);
        //类别属性
        List<Catalogue> catalogues=catalogueService.findAllCatalogue(10001);
        model.addAttribute("catalogues",catalogues);
        model.addAttribute("testPaper",testPaper);
        return "testPaper/testPaper-edit";
    }

    //搜索试卷接口
    @RequestMapping("/search")
    @ResponseBody
    public Map<String,Object> searchTestPaper(TestPaper testPaper,Model model){
        Map<String, Object> map = new HashMap<>();
        System.out.println(testPaper.getName());
        List<TestPaper> testPapers=testPaperService.findTestPaperByCondition(testPaper);
        map.put("testPapers",testPapers);
        model.addAttribute("testPapers",testPapers);
        return map;
    }

    //全部试卷数据接口，前后端分类，前端只需调用该接口就可显示全部试卷的信息，数据格式为json
    @RequestMapping("/findAll")
    @ResponseBody
    public Map<String,Object> findTestPaperAll(){
        Map<String,Object> map=new HashMap<>();
        Map<String,Object> map1=new HashMap<>();
        TestPaper testPaper =new TestPaper();
        //用户id
        String user_id=userService.findUserIdByUsername();
        testPaper.setUser_id(user_id);
        List<TestPaper> testPapers=testPaperService.findTestPaperByCondition(testPaper);
        map1.put("content",testPapers);
        map.put("data",map1);
        map.put("count",testPapers.size());
        map.put("code",0);
        map.put("msg","");
        return map;
    }

    //根据id查询试卷
    @RequestMapping("/findOne")
    @ResponseBody
    public Map<String,Object> findOne(int id){
        Map<String,Object> map=new HashMap<>();
        TestPaper testPaper=testPaperService.findTestPaperById(id);
        List<Question> questions=testPaper.getQuestions();
        map.put("questions",questions);
        return map;
    }

    @RequestMapping("/add-choice")
    public String addChoice(){
        return "testPaper/testPaper-add-choice";
    }

    @RequestMapping("/addManual")
    public String  addTestPaperManual(){
        return "testPaper/testPaper-add-manual";
    }

    @RequestMapping("/addChoice")
    public String addTestPaperChoice(){
        return "testPaper/testPaper-add-choice";
    }

    @RequestMapping("/addAuto")
    public String addTestPaperAuto(){
        return "testPaper/testPaper-add-auto";
    }
}
