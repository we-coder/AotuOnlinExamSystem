package com.btson.aoes.repository;

import com.btson.aoes.domain.Catalogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CatalogueRepository extends JpaRepository<Catalogue,Integer> {
    @Query(nativeQuery=true, value ="select * from catalogue where user_id=?1 ORDER BY id desc limit 1")
    Catalogue findLastCatalogue(int id);


    @Query(nativeQuery=true, value ="select * from catalogue where user_id=?1 ")
    List<Catalogue> findByUser_id(int id);

    @Modifying
    @Query(nativeQuery=true,value="update catalogue c set c.title = ?1 where c.id = ?2")
    int updateCatalogueTitle(String title, int id);

    @Modifying
    void deleteById(int id);

    @Query(nativeQuery = true,value = "select * from catalogue where parent_id=?1")
    List<Catalogue> findByPid(int id);
}
