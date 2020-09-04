package com.btson.aoes.repository;

import com.btson.aoes.domain.BaseDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.annotation.Resource;
import java.util.List;

public interface BaseDictRepository extends JpaRepository<BaseDict,Integer> {

    @Query(nativeQuery=true, value ="select * from base_dict where dict_type_code=?1 ")
    List<BaseDict> findByDict_type_code(String typecode);
}
