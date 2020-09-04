package com.btson.aoes.repository;

import com.btson.aoes.domain.Question_testPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface Question_testPaperRepository extends JpaRepository<Question_testPaper,Integer> {
    @Query(nativeQuery=true, value ="select * from question_testpaper where question_id=?1 and testpaper_id=?2 ")
    Question_testPaper findByQ_idAndT_id(int q_id,int t_id);

    @Query(nativeQuery = true,value = "select sum(score) from question_testpaper where testpaper_id=?1")
    int findTotalScore(int testPaper_id);
}
