package com.btson.aoes.service;

import com.btson.aoes.domain.Options;
import com.btson.aoes.repository.OptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OptionService {
    @Resource
    private OptionRepository optionRepository;

    @Transactional
    public void addOption(List<Options> options){
        optionRepository.saveAll(options);
    }

}
