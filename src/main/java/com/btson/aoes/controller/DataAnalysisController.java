package com.btson.aoes.controller;

import com.btson.aoes.domain.*;
import com.btson.aoes.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/grade")
public class DataAnalysisController {
    @Resource
    private AchievementService achievementService;
    @Resource
    private ExamService examService;
    @Resource
    private UserService userService;
    @Resource
    private ScoreService scoreService;
    @Resource
    private OESClassService oesClassService;
    @Resource
    private TestPaperService testPaperService;
    @Resource
    private AnswerSheetService answerSheetService;
    @Resource
    private UserExamService userExamService;
    @Resource
    private Question_testPaperService question_testPaperService;

    @RequestMapping("/scoreShow")
    public String analysisStu(Model model){
        //当前登录用户id
        int user_id=Integer.parseInt(userService.findUserIdByUsername());
        int[] exam_ids=scoreService.findExamByTeacher(user_id);
        List<Exam> exams=new ArrayList<>();
        for(int exam_id:exam_ids){
            Exam exam=examService.findExamOne(exam_id);
            OESClass oesClass=oesClassService.findOneClass(Integer.parseInt(exam.getClass_id()));
            exam.setClass_id(oesClass.getClass_name());
            exams.add(exam);

        }
        List<Score> scoreList=scoreService.findScoreByTeacher(user_id);

        model.addAttribute("exams",exams);
        model.addAttribute("scores",scoreList);
        return "grade/scoreShow";
    }

    @RequestMapping("/testView")
    public String testView(int user_id,int exam_id,Model model){
        //当前登录用户id
        User user =userService.getUserById(user_id);
        UserExam userExam=userExamService.findUEByUserExam(String.valueOf(user_id),String.valueOf(exam_id));
        if(user.getRoles().get(0).getAuthority().equals("ROLE_STU")){
            if(userExam.getStatus()==0||userExam.getStatus()==2){
                return "redirect:/accessDenied";
            }
        }

        OESClass oesClass=oesClassService.findOneClass(userExam.getClass_id());
        User user1=userService.getUserById(Integer.parseInt(userExam.getUser_id()));
        Exam exam=examService.findExamOne(exam_id);
        testPaperService.showTestPaper(Integer.parseInt(exam.getTestpaper_id()),model);//原试卷
        List<AnswerSheet> answerSheetList=answerSheetService.findListUE(user_id,exam_id);
        List<OESAchievement> achievements=achievementService.findAcByUE(user_id,exam_id);
        Score score=scoreService.findScoreByUE(user_id,exam_id);
        model.addAttribute("score",score);
        model.addAttribute("achievements",achievements);
        model.addAttribute("answerSheets",answerSheetList);
        model.addAttribute("className",oesClass.getClass_name());
        model.addAttribute("stuName",user1.getUsername());
        model.addAttribute("exam",exam);
        return "grade/testView";

    }

    @RequestMapping("/examAnalysis")
    @ResponseBody
    public Map<String,Object> examAnalysis(int exam_id,Model model){
        Map<String,Object> map=new HashMap<>();
        //当前登录用户id
        int teacher_id=Integer.parseInt(userService.findUserIdByUsername());

        //分数情况
        List<Score> scoreList=scoreService.findScoreByET(exam_id,teacher_id);
        List<String> stu_names=new ArrayList<>();
        List<String> stu_scores=new ArrayList<>();
        String exam_name=null;
        for (Score score:scoreList){
            exam_name=score.getExam_name();
            String stu_name=score.getUser_id();
            String stu_score=score.getGrade();
            stu_names.add(stu_name);
            stu_scores.add(stu_score);
        }

        map.put("name",exam_name);
        map.put("stu_names",stu_names);
        map.put("stu_scores",stu_scores);

        //考试情况分析
        List<UserExam> userExamList=userExamService.findUEByExam(exam_id);
        int submit=0,unsubmit=0;
        List<Integer> subs=new ArrayList<>();
        for (UserExam userExam:userExamList){
            if (userExam.getStatus()==1){
                submit++;
            }else {
                unsubmit++;
            }

        }
        subs.add(submit);
        subs.add(unsubmit);
        map.put("subs",subs);

        //答题情况
        List<OESAchievement> achievementList=achievementService.findAcByExam(exam_id);
        int rq1=0,rq2=0,rq3=0,rq4=0,wq1=0,wq2=0,wq3=0,wq4=0,score1=0,score2=0,score3=0,score4=0;
        List<Integer> rq=new ArrayList<>();
        List<Integer> wq=new ArrayList<>();
        List<Integer> scores=new ArrayList<>();
        for (OESAchievement achievement:achievementList){
            if (achievement.getTeacher_id().equals("单选题")){
                if (achievement.getScore()==0){
                    wq1++;
                }else {
                    rq1++;
                    score1=score1+achievement.getScore();
                }
            }else if (achievement.getTeacher_id().equals("多选题")){
                if (achievement.getScore()==0){
                    wq2++;
                }else {
                    rq2++;
                    score2=score2+achievement.getScore();
                }
            }else if (achievement.getTeacher_id().equals("判断题")){
                if (achievement.getScore()==0){
                    wq3++;
                }else {
                    rq3++;
                    score3=score3+achievement.getScore();
                }
            }else{
                if (achievement.getScore()==0){
                    wq4++;
                }else {
                    rq4++;
                    score4=score4+achievement.getScore();
                }
            }
        }
        rq.add(rq1);rq.add(rq2);rq.add(rq3);rq.add(rq4);
        wq.add(wq1);wq.add(wq2);wq.add(wq3);wq.add(wq4);
        scores.add(score1);scores.add(score2);scores.add(score3);scores.add(score4);
        map.put("rq",rq);
        map.put("wq",wq);
        map.put("scores",scores);


        return map;
    }

    @RequestMapping("/dataAnalysis")
    public String dataAnalysis(Model model, HttpServletRequest request){
        String url=request.getQueryString();
        //当前登录用户id
        int teacher_id=Integer.parseInt(userService.findUserIdByUsername());

        //全部考试
        int[] exam_ids=scoreService.findExamByTeacher(teacher_id);
        List<Exam> examList=new ArrayList<>();
        for (int eid:exam_ids){
            Exam exam=examService.findExamOne(eid);
            examList.add(exam);
        }
        model.addAttribute("examList",examList);
        if (url==null){

        }else {
            String exam_id=url.substring(8);
            //分数情况
            float scoreAll=0;
            List<Score> scoreList=scoreService.findScoreByET(Integer.parseInt(exam_id),teacher_id);
            for (Score score:scoreList){
                scoreAll=scoreAll+Integer.parseInt(score.getGrade());
            }
            scoreAll=scoreAll/scoreList.size();
            Exam exam=examService.findExamOne(Integer.parseInt(exam_id));
            int totalScore=question_testPaperService.findTotalScore(Integer.parseInt(exam.getTestpaper_id()));
            model.addAttribute("stu_num",scoreList.size());//考试人数
            model.addAttribute("totalScore",totalScore);//总分
            model.addAttribute("avg_score",scoreAll);//平均分
            model.addAttribute("exam_id", exam_id);
        }

        return "grade/analysis-teacher";
    }

}
