package com.btson.aoes.service;

import com.btson.aoes.domain.Catalogue;
import com.btson.aoes.repository.CatalogueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogueService {
    @Resource
    CatalogueRepository catalogueRepository;

    @Transactional
    public Catalogue addCatalogue(Catalogue catalogue,int ids,int user_id){
        catalogue.setUser_id(user_id);
        catalogue.setParent_id(ids);
        catalogue.setSpread("true");
        catalogueRepository.save(catalogue);
        Catalogue catalogue1=catalogueRepository.findLastCatalogue(catalogue.getUser_id());
        return catalogue1;
    }

    public Catalogue saveCatalogue(int user_id){
        Catalogue catalogue=new Catalogue();
        catalogue.setUser_id(user_id);
        catalogue.setTitle("根目录");
        catalogue.setParent_id(0);
        catalogue.setSpread("true");
       return catalogueRepository.save(catalogue);
    }

    @Transactional
    public List<Catalogue> findAllCatalogue(int id){
      return   catalogueRepository.findByUser_id(id);
    }

    //递归，查找父节点
    public List<Catalogue> findParent(int cid){
        List<Catalogue> catalogueList=new ArrayList<>();
        if(cid==1){
            Catalogue catalogue=catalogueRepository.getOne(cid);
            catalogueList.add(catalogue);
            return catalogueList;
        }else {
            Catalogue catalogue=catalogueRepository.getOne(cid);
            catalogueList.add(catalogue);
            cid=catalogue.getParent_id();
            return findParent(cid);
        }
    }
    //递归，查找子节点
    public List<Catalogue> findNode(int cid){
        List<Catalogue> catalogueList=new ArrayList<>();
        List<Catalogue> catalogues=catalogueRepository.findByPid(cid);
        if(catalogues.size()==0){
            return catalogueList;
        }else {
            for (Catalogue catalogue:catalogues){
                cid=catalogue.getParent_id();
                catalogueList.add(catalogue);
                return findNode(cid);
            }
            return catalogueList;
        }

    }

    public Catalogue findOne(int id){
        return catalogueRepository.getOne(id);
    }

    @Transactional
    public int updateCatalogueTitle(String title,int id){
        return catalogueRepository.updateCatalogueTitle(title,id);
    }



    @Transactional
    public void deleteCatalogue(int id){
        catalogueRepository.deleteById(id);
    }

    //自动创建分类
    public void autoAddCatalogue(Catalogue catalogue){
        catalogueRepository.save(catalogue);
    }
}
