package com.btson.aoes.service;

import com.btson.aoes.domain.BaseDict;
import com.btson.aoes.repository.BaseDictRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BaseDictService {
    @Resource
    private BaseDictRepository baseDictRepository;
    //根据类别代码查询数据字典
    public List<BaseDict> findByBaseDict(String BaseCode){
       return baseDictRepository.findByDict_type_code(BaseCode);
    };
}
