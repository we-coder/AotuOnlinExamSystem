package com.btson.aoes.controller;

import cn.hutool.core.date.DateUtil;
import com.btson.aoes.domain.*;
import com.btson.aoes.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cn.hutool.core.date.DateUtil;

//examinees控制层 考生端
@Controller
@RequestMapping("/examinees")
public class ExamineesController {
    @Resource
    ExamService examService;
    @Resource
    UserService userService;
    @Resource
    UserExamService userExamService;
    @Resource
    AnswerSheetService answerSheetService;
    @Resource
    TestPaperService testPaperService;
    @Resource
    ScoreService scoreService;
    @Resource
    OESClassService classService;
    @Resource
    NoticeService noticeService;


    public static long tid;

    @RequestMapping("/examAction")
    public String examineesShow(int exam_id,Model model,HttpSession session) {
        String user_id=userService.findUserIdByUsername();
        Exam exam=examService.findExamOne(exam_id);
        UserExam userExam=userExamService.findUEByUserExam(user_id,String.valueOf(exam_id));
        //设置考试状态：正在进行
        userExam.setStatus(2);
        userExamService.saveUserExamOne(userExam);
        //设置考试已读通知
        Notice notice=noticeService.findNoticeByRU(user_id,String.valueOf(exam.getId()));
        if(notice!=null){
            notice.setStatus(1);
            noticeService.savaNotice(notice);
            List<Notice> notices=noticeService.findNoticeByRS(user_id,0);
            session.setAttribute("NOTICE_SESSION",notices);
        }


        testPaperService.showTestPaper(Integer.parseInt(exam.getTestpaper_id()),model);
        model.addAttribute("exam",exam);
        model.addAttribute("exam_duration",userExam.getDuration());
        return "examinees/examAction";
    }
    @RequestMapping("/grade")
    public String analysisTest(TestPaper testPaper, Model model, HttpServletRequest request){

        //查询判断
        if(testPaper.getType()==""){
            testPaper.setType(null);
        }
        if(testPaper.getName()==""){
            testPaper.setName(null);
        }
        HttpSession session=request.getSession();
       User user= (User) session.getAttribute("USER_SESSION");

        List<Score> scores=scoreService.findScoreAll(user.getId());
        model.addAttribute("testPaper_type",testPaper.getType());
        model.addAttribute("testPaper_name",testPaper.getName());
        model.addAttribute("Scores",scores);
        return "examinees/gradeShow";
    }

    @RequestMapping("/analysis")
    public String analysisAnalysis(){
        return "examinees/stu-analysis";

    }

    @RequestMapping("/manage")
    public String examineesManage(Model model){
        String user_id=userService.findUserIdByUsername();
        List<UserExam> userExamList =userExamService.findUEByUserId(user_id);
        List<Integer> userSum=new ArrayList<>();
        List<Integer> userNew=new ArrayList<>();
        List<Integer> userOld=new ArrayList<>();
        List<Integer> userIng=new ArrayList<>();

        for (UserExam userExam:userExamList){
//            OESClass oesClass=classService.findOneClass(userExam.getClass_id());
//            String class_id=oesClass.getClass_name();
//            userExam.setClass_id(class_id);
            if (userExam.getStatus()==0){
                userNew.add(Integer.parseInt(userExam.getExam_id()));
                userSum.add(Integer.parseInt(userExam.getExam_id()));

            }else if(userExam.getStatus()==1){
                userOld.add(Integer.parseInt(userExam.getExam_id()));
                userSum.add(Integer.parseInt(userExam.getExam_id()));
            }else if(userExam.getStatus()==2){
                userIng.add(Integer.parseInt(userExam.getExam_id()));
                userSum.add(Integer.parseInt(userExam.getExam_id()));
            }
        }
        List<Exam> examListNew=examService.findExamineesExamAll(userNew);
        List<Exam> examListOld=examService.findExamineesExamAll(userOld);
//        List<Exam> examList=examService.findExamineesExamAll(userSum);
        List<Exam> examListIng=examService.findExamineesExamAll(userIng);
        model.addAttribute("examListNew",examListNew);
        model.addAttribute("examListOld",examListOld);
        model.addAttribute("examListIng",examListIng);
//        model.addAttribute("examList",examList);

        return "examinees/manage";
    }

    //考生提交试卷
    @RequestMapping("/addAnswerSheet")
    @ResponseBody
    public String examAdd(String[] opt_id,int[] opt_status,String[] answer,int exam_id) throws InterruptedException {
        String user_id=userService.findUserIdByUsername();
        Exam exam=examService.findExamOne(exam_id);
        List<AnswerSheet> answerSheetList=new ArrayList<>();
        int i=0;
        for(String o_id:opt_id) {
            AnswerSheet answerSheet=new AnswerSheet();
            answerSheet.setOption_id(o_id);
            answerSheet.setUser_id(user_id);
            answerSheet.setCreate_time(new Date());
            answerSheet.setTeacher_id(exam.getTeacher_id());
            answerSheet.setExam_id(String.valueOf(exam_id));
            //判断是否为主观题
                if (answer!=null&&opt_id.length - i == answer.length) {
                    int j=i;
                    for (String ans : answer) {
                        AnswerSheet answerSheet2=new AnswerSheet();
                        answerSheet2.setOption_id(opt_id[j]);
                        answerSheet2.setUser_id(user_id);
                        answerSheet2.setCreate_time(new Date());
                        answerSheet2.setTeacher_id(exam.getTeacher_id());
                        answerSheet2.setExam_id(String.valueOf(exam_id));
                        answerSheet2.setAnswer(ans);
                        //主观题
                        answerSheetList.add(answerSheet2);
                        j++;
                    }
                    break;
                } else {
                    //选择题，判断题
                    if (opt_status==null || opt_status.length==0){
                    }else{
                        answerSheet.setOpt_status(opt_status[i]);
                        answerSheetList.add(answerSheet);
                    }

                }
            i++;
            }

        answerSheetService.saveASAll(answerSheetList);

        // 考生提交考试，该考试变为已考状态
        UserExam userExam=userExamService.findUEByUserExam(user_id,String.valueOf(exam_id));
        userExam.setStatus(1);
        boolean threadstatus=false;
        String threadName=user_id+"-"+exam_id+"-"+userExam.getId();   //线程名字
        findThreadByName(threadName,threadstatus);
        userExamService.saveUserExamOne(userExam);

        return "OK";
    }

    /**
     * 学生扫二维码加入班级
     */
    @RequestMapping("/addClass")
    @ResponseBody
    public String addClass(int class_id){
        //当前登录用户
        String user_id=userService.findUserIdByUsername();
        OESClass oesClass=classService.findOneClass(class_id);
        User user=userService.getUserById(Integer.parseInt(user_id));
        List<User> users=oesClass.getUsers();
        List<Integer> ids=new ArrayList<>();
        for (User user1:users){
            ids.add(user1.getId());
        }
        ids.add(user.getId());
        int[] uids=ids.stream().mapToInt(Integer::valueOf).toArray();
        classService.classUpdate(oesClass,uids);
        return "OK";
    }

    /**
     * 成功加入班级
     */
    @RequestMapping("/toAddClass")
    public String toAddClass(Model model,int class_id){
        OESClass oesClass=classService.findOneClass(class_id);
        model.addAttribute("class",oesClass);
        return "examinees/joinClass";
    }

    @RequestMapping("/countdown")
    @ResponseBody
    public void examCountdown(String exam_id) throws ParseException, InterruptedException {
        String user_id=userService.findUserIdByUsername();
        UserExam userExam=userExamService.findUEByUserExam(user_id,exam_id);
        String threadName=user_id+"-"+exam_id+"-"+userExam.getId();   //线程名字
        boolean threadstatus=true;
        if (findThreadByName(threadName,threadstatus)){  //判断该考试的倒计时线程是否在进行，一个考生一场考试对应一个线程，
            return;
        }
        Thread.currentThread().setName(threadName);          //设置该线程的名称
        String times=userExam.getDuration();
        int time= DateUtil.timeToSecond(times);
        while (time > 0) {
            time--;
            try {
                tid=Thread.currentThread().getId();
                Thread.sleep(1000);
                int hh = time / 60 / 60 % 60;
                int mm = time / 60 % 60;
                int ss = time % 60;
                String duration=hh+":"+mm+":"+ss;
                if(hh==0&&mm==0&&ss==0){

                    userExam.setStatus(1);
                    userExam.setDuration(duration);
                    userExamService.saveUserExamOne(userExam);

                }else {
                    userExam.setDuration(duration);
                    userExamService.saveUserExamOne(userExam);
                }
                System.out.println("线程："+Thread.currentThread().getName()+",学号："+user_id+"，考试场次:"+exam_id+"，倒计时："+duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public static boolean findThreadByName(String threadName,boolean threadstatus) throws InterruptedException {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;

        /* 遍历线程组树，获取根线程组 */
        while ( group != null )
        {
            topGroup= group;
            group=group.getParent();
        }
        /* 激活的线程数加倍 */
        int estimatedSize = topGroup.activeCount() * 2;
        Thread[] slackList = new Thread[estimatedSize];

        /* 获取根线程组的所有线程 */
        int actualSize = topGroup.enumerate( slackList );
        /* copy into a list that is the exact size */
        Thread[] list = new Thread[actualSize];
        System.arraycopy( slackList, 0, list, 0, actualSize );
        for (Thread thread:list){
            if(thread.getName().equals(threadName)){
                if(threadstatus){
                    return true;
                }else {
                    thread.stop();
                    thread.interrupt();
                }


            }
        }

        return false;
    }

}
