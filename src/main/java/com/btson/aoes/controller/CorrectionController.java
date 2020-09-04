package com.btson.aoes.controller;

import com.btson.aoes.domain.*;
import com.btson.aoes.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/correction")
public class CorrectionController {
    @Resource
    private AnswerSheetService answerSheetService;
    @Resource
    private ExamService examService;
    @Resource
    private UserExamService userExamService;
    @Resource
    private TestPaperService testPaperService;
    @Resource
    private AchievementService achievementService;
    @Resource
    private Question_testPaperService question_testPaperService;
    @Resource
    private ScoreService scoreService;


    /*
    系统自动批改（选择题，判断题）
     */
    @RequestMapping("/auto")
    public String correctionTest(){
        //查找考试号，考生答题卡考试号a，参考答案试卷号b
        List<OESAchievement> oesAchievements=new ArrayList<>();
        List<AnswerSheet> answerSheets=new ArrayList<>();
        int[] examIdList= answerSheetService.findExamIds();
        for(int exam_id:examIdList){
            Exam exam=examService.findExamOne(exam_id); //获取当前考试场次正确id
            int testPaper_id=Integer.parseInt(exam.getTestpaper_id());
            //正确答案
            TestPaper testPaper=testPaperService.findTestPaperById(testPaper_id);
            List<Question> questionList=testPaper.getQuestions();
            List<AnswerSheet> answerSheetList=answerSheetService.findAllExam(exam_id);
            int i=0;
            for (Question question:questionList){
                int rightCount=0;
                if(question.getQuestion_type().equals("单选题")||question.getQuestion_type().equals("多选题")||question.getQuestion_type().equals("判断题")){
                    List<Options> optionsList = question.getOptions(); //单道题选项数据
                    OESAchievement achievement = new OESAchievement();
                    achievement.setQuestion_id(question.getId());
                    achievement.setTeacher_id(question.getUser_id());
                    achievement.setExam_id(String.valueOf(exam_id));
                    achievement.setStatus(1);    //设置分数可显示状态
                    achievement.setCreate_time(new Date());
                    for(Options option:optionsList){
                        for(AnswerSheet answerSheet:answerSheetList){
                            achievement.setUser_id(answerSheet.getUser_id());//考试考生
                            if(option.getOpt_id()==Integer.parseInt(answerSheet.getOption_id())){
                                answerSheet.setCorrection_status(1); //批改状态
                                if(option.getOpt_status()==answerSheet.getOpt_status()){
                                    //正确
                                    answerSheets.add(answerSheet);
                                    rightCount++;
                                }
                            }
                        }
                    }
                    //判断正确个数，全对加分
                    if(rightCount==optionsList.size()){
                        if(testPaper_id!=0) {
                            Question_testPaper qtId = question_testPaperService.findQTId(question.getId(), testPaper_id);
                            int score = qtId.getScore();
                            achievement.setScore(score);
                        }
                        oesAchievements.add(achievement);
                    }else {
                        achievement.setScore(0);
                        oesAchievements.add(achievement);
                    }

                }
                i++;
            }

        }
        //利用a比较b
        //
        List<OESAchievement> oesAchievementList = achievementService.saveAchievement(oesAchievements);
        List<AnswerSheet> answerSheetList1=answerSheetService.saveASAll(answerSheets);

        return "redirect:/examinees/manage";
    }


    /*
    人工手动批改(主观题)
     */
    @RequestMapping("/Manual")
    @ResponseBody
    public String correctionManual(int[] option_id,int[] score,int[] question_id,int user_id,int exam_id){
        List<AnswerSheet> answerSheetList=new ArrayList<>();
        List<OESAchievement> achievementList=new ArrayList<>();
        int i=0;
        for (int opt_id:option_id){
            OESAchievement achievement=new OESAchievement();
            OESAchievement achievement1=achievementService.findAchievementByUE(user_id,exam_id,question_id[i]);
            AnswerSheet answerSheet=answerSheetService.findOneOUE(opt_id,user_id,exam_id);
            answerSheet.setCorrection_status(2);//1:系统批改，2:手工批改，0：未批改
            if(achievement1==null){

            }else {
                achievement.setId(achievement1.getId());
            }
            achievement.setScore(score[i]);
            achievement.setUser_id(String.valueOf(user_id));
            achievement.setTeacher_id(answerSheet.getTeacher_id());
            achievement.setCreate_time(new Date());
            achievement.setStatus(1);
            achievement.setExam_id(String.valueOf(exam_id));
            achievement.setQuestion_id(question_id[i]);
            answerSheetList.add(answerSheet);
            achievementList.add(achievement);

            i++;
        }
        List<AnswerSheet> answerSheets=answerSheetService.saveASAll(answerSheetList);
        List<OESAchievement> achievements=achievementService.saveAchievement(achievementList);

        return "OK";
    }

    //获取答案数据
    @RequestMapping("/getAnswer")
    @ResponseBody
    public Map<String,Object> getAnswer(int user_id, int exam_id,int cs){
        Map<String,Object> map=new HashMap<>();
        Exam exam=examService.findExamOne(exam_id); //获取当前考试场次正确id
        int testPaper_id=Integer.parseInt(exam.getTestpaper_id());
        //正确答案
        TestPaper testPaper=testPaperService.findTestPaperById(testPaper_id);
        List<Question> questionList=testPaper.getQuestions();
        List<Question> answers=new ArrayList<>();
        List<AnswerSheet> answerSheetList=answerSheetService.findAllUE(user_id,exam_id,cs);

        for (Question question:questionList){
            if(question.getQuestion_type().equals("主观题")){
                for (Options option:question.getOptions()){
                    for (AnswerSheet  answerSheet:answerSheetList){
                        if(option.getOpt_id()==Integer.parseInt(answerSheet.getOption_id())){
                            OESAchievement achievement=achievementService.findAchievementByUE(user_id,exam_id,question.getId());
                            Question question1=new Question();
                            //判断是否存在已批改数据
                            if(achievement==null){

                            }else {
                                question1.setQuestion_subject(String.valueOf(achievement.getScore()));
                            }
                            question1.setId(Integer.parseInt(answerSheet.getOption_id()));
                            question1.setQuestion_describe(String.valueOf(question.getId()));
                            question1.setQuestion_type(question.getQuestion_type());
                            question1.setQuestion_title(question.getQuestion_title());
                            question1.setAnswer(answerSheet.getAnswer());
                            question1.setQuestion_analysis(option.getOpt_content());
                            Question_testPaper qtId = question_testPaperService.findQTId(question.getId(), testPaper_id);
                            int score = qtId.getScore();
                            question1.setQuestion_content(String.valueOf(score));
                            answers.add(question1);
                        }
                    }

                }
            }
        }



        map.put("user_id",user_id);
        map.put("exam_id",exam_id);
        map.put("question",answers);
        return map;

    }





}
