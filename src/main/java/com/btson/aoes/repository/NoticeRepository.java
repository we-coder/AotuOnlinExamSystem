package com.btson.aoes.repository;

import com.btson.aoes.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {

    @Query(nativeQuery = true, value = "select * from notice where recipient=?1 ")
    List<Notice> findNoticeByRecipient(String recipient_id);

    @Query(nativeQuery = true, value = "select * from notice where recipient=?1 and status=?2")
    List<Notice> findNoticeByStatus(String recipient_id, int status);

    @Query(nativeQuery = true, value = "select * from notice where create_date=?1 and create_id=?2")
    List<Notice> findNoticeByDate(Date create_date, String create_id);

    @Query(nativeQuery = true, value = "select max(id) id,max(title) title,max(content) content,create_date,max(recipient) recipient,max(url),max(type) type,max(status) status,max(role_obj) role_obj,max(create_id) create_id,max(url) url  from notice where create_id=?1 group by create_date")
    List<Notice> findCreaterNotice(String create_id);

    @Query(nativeQuery = true, value = "select * from notice where recipient=?1 and url=?2")
    Notice findNoticeByRU(String recipient_id, String url);

}