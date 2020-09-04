package com.btson.aoes.repository;

import com.btson.aoes.domain.UserExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserExamRepository extends JpaRepository<UserExam,Integer> {
    @Query(nativeQuery=true, value ="select * from userexam where user_id=?1 ")
    List<UserExam> findByUser_id(String id);
    @Query(nativeQuery=true, value ="select * from userexam where user_id=?1 and exam_id=?2")
    UserExam findUEByUserAndExam(String user_id,String exam_id);

    @Query(nativeQuery = true,value = "select * from userexam where exam_id=?1")
    List<UserExam> findByExam_id(int id);

    @Query(nativeQuery = true,value = "select * from userexam where exam_id=?1 and class_id=?2")
    List<UserExam> findByEidAndCid(String exam_id,String class_id);
}
