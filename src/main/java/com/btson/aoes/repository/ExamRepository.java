package com.btson.aoes.repository;


import com.btson.aoes.domain.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam,Integer> {
    @Query(nativeQuery=true, value ="select * from exam where teacher_id=?1 and exam_status=0 and end_time is not NULL")
    List<Exam> findExamByUser(String id);
}
