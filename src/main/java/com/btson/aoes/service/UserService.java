package com.btson.aoes.service;

import com.btson.aoes.domain.OESRole;
import com.btson.aoes.domain.User;
import com.btson.aoes.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 需要实现UserDetailsService接口
 * 因为在Spring Security中配置的相关参数需要是UserDetailsService类型的数据
 * */
@Service
public class UserService implements UserDetailsService {
    @Resource
    private UserRepository userRepository;

    @Transactional
    public void save(User user){

        userRepository.save(user);
    }

    public List<User> getAllUser(){
        return userRepository.getAll();
    }

    public User getUserByNameAndPwd(String name,String password){
        return userRepository.findByUsernameAndPassword(name,password);
    }
    public void deleteUser(int id){
        userRepository.deleteById(id);
    }

    public void updateUser(User user){
        userRepository.save(user);
    }


    public User getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    /*
     *  重写UserDetailsService接口中的loadUserByUsername方法，通过该方法查询到对应的用户(non-Javadoc)
     *  返回对象UserDetails是Spring Security中一个核心的接口。
     *  其中定义了一些可以获取用户名、密码、权限等与认证相关的信息的方法。
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 调用持久层接口findByLoginName方法查找用户，此处的传进来的参数实际是loginName
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 创建List集合，用来保存用户权限，GrantedAuthority对象代表赋予给当前用户的权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 获得当前用户权限集合
        List<OESRole> roles = user.getRoles();
        for (OESRole role : roles) {
            // 将关联对象Role的authority属性保存为用户的认证权限
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        // 此处返回的是org.springframework.security.core.userdetails.User类，该类是Spring Security内部的实现
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    //查找登录用户id
    public String findUserIdByUsername(){
        // Authentication是一个接口，表示用户认证信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
        User user=getUserByName(auth.getName());
        return String.valueOf(user.getId());
    }

    public User getUserById(int id){
        return userRepository.findById(id);
    }

}
