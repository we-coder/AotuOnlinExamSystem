package com.btson.aoes.controller;

import com.btson.aoes.domain.Notice;
import com.btson.aoes.domain.User;
import com.btson.aoes.service.NoticeService;
import com.btson.aoes.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    private NoticeService noticeService;
    @Resource
    private UserService userService;

    @RequestMapping("/manage")
    public String manageNotice(Model model){
        //当前登录用户id
        String user_id=userService.findUserIdByUsername();
        List<Notice> notices=noticeService.findCreateNotice(user_id);
        model.addAttribute("notices",notices);
        return "notice/notice-manage";
    }
    @RequestMapping("/details")
    public String detailsNotice(int id,Model model){
        Notice notice=noticeService.findNoticeById(id);
        model.addAttribute("notice",notice);
        return "notice/notice-details";
    }
    @RequestMapping("/delete")
    @ResponseBody
    public String deleteNotice(int id){
        Notice notice=noticeService.findNoticeById(id);
        List<Notice> noticeList=noticeService.findCreateOne(notice.getCreate_id(),notice.getCreate_date());
        noticeService.deleteNotice(noticeList);
        return "<script>alert(\"删除成功\");window.location.href='/notice/manage';</script>";
    }

    @RequestMapping("/edit")
    public String editNotice(){
        return "admin/notice-create";
    }
    @RequestMapping("/add")
    @ResponseBody
    public String addNotice(String title,String content, String recipient){
        //当前登录用户id
        String user_id=userService.findUserIdByUsername();
        List<Notice> noticeList=new ArrayList<>();
        List<User> userList=userService.getAllUser();
        for (User user:userList){
            Notice notice=new Notice();
            notice.setTitle(title);
            notice.setContent(content);
            notice.setStatus(0);
            notice.setCreate_date(new Date());
            notice.setCreate_id(user_id);
            notice.setType("公告");
            String au=null;
            if (user.getRoles().size()==0){

            }else {
                au = user.getRoles().get(0).getAuthority();
                if (recipient.equals("0")  && au.equals("ROLE_STU")) {
                    notice.setRecipient(String.valueOf(user.getId()));
                    notice.setRole_obj("考生");
                    noticeList.add(notice);

                } else if (recipient.equals("1") && au.equals("ROLE_TEACHER")) {
                    notice.setRecipient(String.valueOf(user.getId()));
                    notice.setRole_obj("教师");
                    noticeList.add(notice);

                } else if (recipient.equals("2")){
                    notice.setRecipient(String.valueOf(user.getId()));
                    notice.setRole_obj("全体用户");
                    noticeList.add(notice);
                }
            }
        }

        noticeService.addNotices(noticeList);

            return "OK";
    }

    @RequestMapping("/user")
    public String stuNotice(Model model){
        //当前登录用户id
        String user_id=userService.findUserIdByUsername();
        List<Notice> notices=noticeService.findNoticeByStu(user_id);
        model.addAttribute("notices",notices);
        return "notice/notice-user";
    }

    @RequestMapping("/user/read")
    public String userRead(int id, Model model, HttpSession session){
        //当前登录用户id
        String user_id=userService.findUserIdByUsername();
        Notice notice=noticeService.findNoticeById(id);
        notice.setStatus(1);//已读
        Notice notice1=noticeService.savaNotice(notice);
        model.addAttribute("notice",notice1);
        List<Notice> notices=noticeService.findNoticeByRS(user_id,0);
        session.setAttribute("NOTICE_SESSION",notices);
        return "notice/notice-details";
    }
}
