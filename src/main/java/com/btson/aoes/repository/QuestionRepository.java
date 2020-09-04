package com.btson.aoes.repository;

import com.btson.aoes.domain.Question;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Integer>{
    @Query(nativeQuery=true, value ="select * from question where user_id=?1 ORDER BY create_time desc limit 1")
    Question findLastQuestion(String id);

    @Query(nativeQuery = true,value = "select * from question where user_id=?1 and source=?2")
    List<Question> findUploadQuestions(int user_id,String source);

    @Query(nativeQuery = true,value = "select * from question where  question_class  like ?1 and question_level like ?2 and question_type like ?3  and user_id=?5 order by rand() limit ?4")
    List<Question> findByRandom(String question_class, String question_level, String question_type, int num,int user_id);

    void deleteById(int id);

    Question findById(int id);

}
