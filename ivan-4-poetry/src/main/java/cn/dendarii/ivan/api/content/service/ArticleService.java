package cn.dendarii.ivan.api.content.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dendarii.ivan.api.content.bean.mongo.HBArticle;
import cn.dendarii.ivan.api.content.dao.ArticleDao;
import cn.dendarii.ivan.common.dao.l2.BaseCRUDDao;
import cn.dendarii.ivan.common.service.BaseCRUDService;

@Service
public class ArticleService extends BaseCRUDService<HBArticle> {
    @Resource
    private ArticleDao articleDao;

    public BaseCRUDDao<HBArticle> dao() {
        return articleDao;
    }

}