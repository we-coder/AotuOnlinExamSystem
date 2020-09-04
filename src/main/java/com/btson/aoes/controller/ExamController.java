package com.btson.aoes.controller;

import com.btson.aoes.domain.*;
import com.btson.aoes.service.*;
import com.btson.aoes.tools.CurrentTimer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exam")
public class ExamController {
    @Resource
    ExamService examService;
    @Resource
    OESClassService oesClassService;
    @Resource
    UserExamService userExamService;
    @Resource
    TestPaperService testPaperService;
    @Resource
    UserService userService;
    @Resource
    AnswerSheetService answerSheetService;
    @Resource
    NoticeService noticeService;

    @RequestMapping("/add")
    public String examAdd(Exam exam, HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        //利用session获取当前用户账号密码
        HttpSession session=request.getSession();
        User user2= (User) session.getAttribute("USER_SESSION");
        String userId1=String.valueOf(user2.getId());
        exam.setTeacher_id(userId1);
        Date date=CurrentTimer.getCurrentTime();
        exam.setCreate_time(date);
        //查找班级数据
        int class_id=Integer.parseInt(exam.getClass_id());
        OESClass oesClass=oesClassService.findOneClass(class_id);

        //添加考试，并返回该考试对象
        Exam exam1=examService.addExam(exam);
        //查找该班级的用户，并存入userexam表中
        List<UserExam> userExamList=new ArrayList<>();
        List<Notice> notices=new ArrayList<>();
        for(User user:oesClass.getUsers()){
            UserExam userExam=new UserExam();
            userExam.setExam_id(String.valueOf(exam1.getId()));
            userExam.setUser_id(String.valueOf(user.getId()));
            userExam.setClass_id(class_id);
            userExam.setDuration(exam1.getDuration());
            userExamList.add(userExam);
            Notice notice=new Notice();
            notice.setStatus(0);
            notice.setCreate_id(userId1);
            notice.setRole_obj("考生");
            notice.setContent(exam1.getExam_name());
            notice.setRecipient(String.valueOf(user.getId()));
            notice.setType("考试");
            notice.setUrl(String.valueOf(exam1.getId()));
            notice.setCreate_date(new Date());
            notices.add(notice);
        }
        List<UserExam> userExams=userExamService.saveUserExamAll(userExamList);
        List<Notice> noticeList=noticeService.addNotices(notices);
        //判断考试是否生成
        if(exam1.getId()>0){

            return "redirect:/exam/manage";
        }else{
            return "redirect:/exam/error";
        }

    }

    @RequestMapping("/set")
    public String setExam(Model model){
        //利用session获取当前用户账号密码
        /*HttpSession session=request.getSession();
        User user= (User) session.getAttribute("USER_SESSION");
        String userId1=String.valueOf(user.getId());*/
        OESClass oesClass=new OESClass();
        oesClass.setClass_teacher(userService.findUserIdByUsername());
        //查询判断
        if(oesClass.getClass_name()==""){
            oesClass.setClass_name(null);
        }

        List<OESClass> OESClasses = oesClassService.findByCondition(oesClass);
        model.addAttribute("classList",OESClasses);
        return "exam/exam-add";

    }
    @RequestMapping("/manage")
    public String setExam2(Model model){
        //当前登录用户id
        String user_id=userService.findUserIdByUsername();
        //当前用户创建的考试数据，权限为老师
        Exam exam=new Exam();
        exam.setTeacher_id(user_id);
        List<Exam> examList=examService.findExamsByCondition(exam);
        examService.examShow(examList,model);
        return "exam/exam-manage";
    }

    @RequestMapping("/examAction")
    public String examAction(int id,Model model){
        Exam exam=examService.findExamOne(id);

        testPaperService.showTestPaper(Integer.parseInt(exam.getTestpaper_id()),model);
        model.addAttribute("exam",exam);
        return "exam/exam-action";
    }
    @RequestMapping("/examSituation")
    public String examSituation(int exam_id,Model model){
        Exam exam=examService.findExamOne(exam_id);
        String class_id=exam.getClass_id();
        OESClass oesClass=oesClassService.findOneClass(Integer.parseInt(class_id));
        List<UserExam> userExamList=userExamService.findUEByEidCid(String.valueOf(exam_id),class_id);
        List<UserExam> userExams=new ArrayList<>();
        for (UserExam userExam:userExamList){
            User user=userService.getUserById(Integer.parseInt(userExam.getUser_id()));
            userExam.setId(Integer.parseInt(userExam.getUser_id()));
            userExam.setUser_id(user.getUsername());
            userExams.add(userExam);
        }
        model.addAttribute("userExams",userExams);
        model.addAttribute("exam",exam);
        model.addAttribute("oesClass",oesClass);
        return "exam/exam-situation";
    }

    @RequestMapping("/correction")
    public String examCorrection(Model model){
        /*
        未改
         */
       //当前登录用户id
        int teacher_id=Integer.parseInt(userService.findUserIdByUsername());
        //获取该老师的所有考试场次id
        List<AllBean> allBeans=new ArrayList<>();
        int[] examIds=answerSheetService.findAllTeacher(0,teacher_id);
        //根据id查找exam实体
        List<Integer> exam_ids= Arrays.stream(examIds).boxed().collect(Collectors.toList());;
        List<Exam> exams=examService.findExamineesExamAll(exam_ids);
        model.addAttribute("exams",exams);
        //未批改学生数据
        List<Object> objects=answerSheetService.findCorrectionData(0,teacher_id);
            for (Object object:objects){
                Object[] obj=(Object[])object;
                String user_id=obj[0].toString();
                String exam_id=obj[1].toString();
                UserExam userExam=userExamService.findUEByUserExam(user_id,exam_id);
                AllBean allBean=new AllBean();
                User user=userService.getUserById(Integer.parseInt(user_id));
                OESClass oesClass=oesClassService.findOneClass(userExam.getClass_id());
                allBean.setExam_id(String.valueOf(exam_id));
                allBean.setUsername(user.getUsername());
                allBean.setUser_id(String.valueOf(user.getId()));
                allBean.setClass_id(oesClass.getClass_name()); //班级

                allBeans.add(allBean);
            }
        model.addAttribute("answerSheetList",allBeans);

        /*
        已改
         */
        int[] examIds2=answerSheetService.findAllTeacher(2,teacher_id); //查找手工批改的试卷
        List<Integer> exam_ids2= Arrays.stream(examIds2).boxed().collect(Collectors.toList());;
        List<AllBean> allBeans2=new ArrayList<>();
        List<Exam> exams2=examService.findExamineesExamAll(exam_ids2);
        model.addAttribute("exams2",exams2);
        //未批改学生数据
        List<Object> objectList=answerSheetService.findCorrectionData(2,teacher_id);
        for (Object object:objectList){
            Object[] obj=(Object[])object;
            String user_id=obj[0].toString();
            String exam_id=obj[1].toString();
            UserExam userExam=userExamService.findUEByUserExam(user_id,exam_id);
            AllBean allBean=new AllBean();
            User user=userService.getUserById(Integer.parseInt(user_id));
            OESClass oesClass=oesClassService.findOneClass(userExam.getClass_id());
            allBean.setExam_id(String.valueOf(exam_id));
            allBean.setUsername(user.getUsername());
            allBean.setUser_id(String.valueOf(user.getId()));
            allBean.setClass_id(oesClass.getClass_name()); //班级

            allBeans2.add(allBean);
        }
        model.addAttribute("answerSheetList2",allBeans2);


        return "exam/correction";
    }

}
