package com.btson.aoes.repository;

import com.btson.aoes.domain.TestPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestPaperRepository extends JpaRepository<TestPaper,Integer> {
    TestPaper findById(int id);

    @Query(nativeQuery=true, value ="select * from testpaper where user_id=?1 ORDER BY create_time desc limit 1")
    TestPaper findLastTestPaper(String id);

    @Query(nativeQuery=true, value ="select * from testpaper where user_id=?1 ORDER BY create_time desc")
    List<TestPaper> findAllTestPaperByTid(String id);



}
