package com.btson.aoes.repository;

import com.btson.aoes.domain.OESAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AchievementRepository extends JpaRepository<OESAchievement,Integer> {

    @Query(nativeQuery = true,value = "select * from achievement where user_id=?1 and exam_id=?2 and question_id=?3")
    OESAchievement findAchievementByUE(int user_id,int exam_id,int question_id);

    @Query(nativeQuery = true,value = "select * from achievement where user_id=?1 and exam_id=?2")
    List<OESAchievement> findAcByUE(int user_id,int exam_id);

    @Query(nativeQuery = true,value = "select a.id,a.user_id,a.exam_id,a.create_time,a.score,a.status,q.question_type as teacher_id,a.question_id from achievement as a left join question q  on a.question_id=q.id where a.exam_id=?1")
    List<OESAchievement> findAcByExam_id(int exam_id);



}
