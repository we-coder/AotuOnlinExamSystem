package com.btson.aoes.service;

import com.btson.aoes.domain.Notice;
import com.btson.aoes.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {
    @Resource
    private NoticeRepository noticeRepository;

    @Transactional
    public Notice savaNotice(Notice notice){
        return noticeRepository.save(notice);
    }
    @Transactional
    public List<Notice> addNotices(List<Notice> notices){
        return noticeRepository.saveAll(notices);
    }
    public List<Notice> findAllNotice(){
        return noticeRepository.findAll();
    }

    //根据发布者查询
    public List<Notice> findCreateNotice(String create_id){
        return noticeRepository.findCreaterNotice(create_id);
    }

    //查询发布者发布的单条，删除
    public List<Notice> findCreateOne(String create_id, Date date){
        return noticeRepository.findNoticeByDate(date,create_id);
    }

    @Transactional
    public void deleteNotice(List<Notice> notices){
        noticeRepository.deleteAll(notices);
    }

    //根据接收对象查询
    public List<Notice> findNoticeByStu(String recipient_id){
        return noticeRepository.findNoticeByRecipient(recipient_id);
    }

    //接收者未读通知 status：0未读，1已读
    public List<Notice> findNoticeByRS(String recipient_id,int status){

        return noticeRepository.findNoticeByStatus(recipient_id,status);
    }
    //接收者考试设置已读通知
    public Notice findNoticeByRU(String recipient_id,String url){
        return noticeRepository.findNoticeByRU(recipient_id,url);
    }


    public Notice findNoticeById(int id){
        return noticeRepository.getOne(id);
    }

}
