package com.btson.aoes.repository;

import com.btson.aoes.domain.AnswerSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerSheetRepository extends JpaRepository<AnswerSheet,Integer> {
    @Query(nativeQuery = true,value = "select * from answer_sheet where user_id=?1 and exam_id=?2")
    List<AnswerSheet> findAllByCS(int user_id,int exam_id);

    @Query(nativeQuery = true,value="select distinct exam_id from answer_sheet where correction_status=0 order by exam_id desc")
    int[] findExamIds();

    @Query(nativeQuery = true,value = "select * from answer_sheet where correction_status=0 and exam_id=?1")
    List<AnswerSheet> findAllByExam(int exam_id);

    @Query(nativeQuery = true,value = "select distinct exam_id from answer_sheet where correction_status=?1 and teacher_id=?2")
    int[] findAllByTeacher(int cs,int teacher_id);

    @Query(nativeQuery = true,value = "select * from answer_sheet where correction_status=?3 and user_id=?1 and exam_id=?2")
    List<AnswerSheet> findAllByUE(int user_id,int exam_id,int cs);

    @Query(nativeQuery = true,value = "select * from answer_sheet where option_id=?1 and user_id=?2 and exam_id=?3")
    AnswerSheet findOneByOUE(int opt_id,int user_id,int exam_id);

    @Query(nativeQuery = true,value = "select user_id,exam_id FROM answer_sheet where correction_status=?1 and teacher_id=?2 GROUP BY user_id,exam_id")
    List<Object> findCorrectionData(int cs,int teacher_id);

}
