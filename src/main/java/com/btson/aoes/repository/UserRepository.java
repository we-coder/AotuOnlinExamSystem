package com.btson.aoes.repository;

import com.btson.aoes.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsernameAndPassword(String username,String password);

    @Query(nativeQuery = true,value = "select * from user")
    List<User> getAll();


    User findByUsername(String username);

    void deleteById(int id);

    User findById(int id);

}
