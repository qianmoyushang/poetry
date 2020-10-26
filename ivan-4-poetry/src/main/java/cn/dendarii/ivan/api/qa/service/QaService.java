package cn.dendarii.ivan.api.qa.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dendarii.ivan.api.content.bean.mongo.HBArticle;
import cn.dendarii.ivan.api.content.dao.ArticleDao;
import cn.dendarii.ivan.api.qa.dao.InvertListDao;
import cn.dendarii.ivan.common.service.BaseService;

@Service
public class QaService extends BaseService {

    @Resource
    private ArticleDao articleDao;

    @Resource
    private InvertListDao invertListDao;

    public void generateInvertList() {
        Collection<HBArticle> articles = articleDao.findAll();
        HashMap<String, ArrayList<String>> word_id_map = new HashMap<String, ArrayList<String>>();

        
    }

}