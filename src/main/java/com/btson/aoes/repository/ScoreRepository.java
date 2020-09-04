package com.btson.aoes.repository;

import com.btson.aoes.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score,Integer> {
    /*
    * ROW_NUMBER() OVER (ORDER BY user_id) AS rowid：以行号作为主键，防止数据重复
    * */
    @Query(nativeQuery=true, value ="SELECT ROW_NUMBER() OVER (ORDER BY user_id) AS rowid,grade,v.user_id,e.exam_name as exam_name,exam_id,v.create_time,v.teacher_id \n" +
            "from v_score as v LEFT JOIN exam as e on v.exam_id=e.id\n" +
            "where v.user_id=?1")
    List<Score> findScoreAll(int user_id);

    @Query(nativeQuery=true, value ="SELECT ROW_NUMBER() OVER (ORDER BY user_id) AS rowid,grade,v.user_id,e.exam_name as exam_name,exam_id,v.create_time,v.teacher_id \n" +
            "from v_score as v LEFT JOIN exam as e on v.exam_id=e.id\n" +
            "where v.exam_id=?1 and v.teacher_id=?2")
    List<Score> findScoreByET(int exam_id,int teacher_id);


    @Query(nativeQuery=true, value ="SELECT ROW_NUMBER() OVER (ORDER BY user_id) AS rowid, grade,v.user_id,u.username as exam_name,exam_id,v.create_time,v.teacher_id \n" +
            "from v_score as v LEFT JOIN exam as e on v.exam_id=e.id left join user as u on u.id=v.user_id\n" +
            "where v.teacher_id=?1")
    List<Score> findScoreAllTeacher(int teacher_id);

    @Query(nativeQuery = true,value = "select distinct exam_id from v_score where teacher_id=?1")
    int[] findExamInScoreByTeacher(int teacher_id);

    @Query(nativeQuery = true,value = "select ROW_NUMBER() OVER (ORDER BY user_id) AS rowid,grade,v.user_id,u.username as exam_name,exam_id,v.create_time,v.teacher_id \n" +
            "         from v_score as v LEFT JOIN exam as e on v.exam_id=e.id left join user as u on u.id=v.user_id where user_id=?1 and exam_id=?2")
    Score findScoreByUE(int user_id,int exam_id);
}
