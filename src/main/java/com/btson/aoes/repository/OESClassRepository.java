package com.btson.aoes.repository;

import com.btson.aoes.domain.OESClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OESClassRepository extends JpaRepository<OESClass,Integer> {
    @Modifying
    @Query(nativeQuery=true, value ="delete from user_class where user_id=?1 and class_id=?2")
    void deleteUserOne(int user_id,int class_id);
}
