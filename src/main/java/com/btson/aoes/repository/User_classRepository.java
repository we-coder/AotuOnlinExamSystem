//package com.btson.aoes.repository;
//
//import com.btson.aoes.domain.User_class;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.List;
//
//
//public interface User_classRepository extends JpaRepository<User_class,Integer> {
//
//   @Modifying
//   @Query(nativeQuery=true, value ="delete from user_class where class_id=?1 and user_id=?2")
//    int deleteByClass_idAndUser_id(int class_id,int user_id);
//
//    @Query(nativeQuery=true, value ="select * from user_class where class_id=?1 and user_id=?2")
//    User_class findUserClassByUC_id(int class_id,int user_id);
//
//    @Modifying
//    @Query(nativeQuery=true, value ="insert into user_class(class_id,user_id) values(?1,?2)")
//    int addUC(int class_id,int user_id);
//
//    @Query(nativeQuery=true, value ="select * from user_class where class_id=?1")
//    List<User_class> findByClass_id(int id);
//}
