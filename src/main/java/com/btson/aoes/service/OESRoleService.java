package com.btson.aoes.service;

import com.btson.aoes.domain.OESRole;
import com.btson.aoes.repository.OESRoleRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OESRoleService {
    @Resource
    OESRoleRepository roleRepository;

    public OESRole findRoleById(int id){
        return roleRepository.getOne(id);
    }
}
