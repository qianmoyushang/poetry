package cn.dendarii.ivan.api.content.dao;

import org.springframework.stereotype.Repository;

import cn.dendarii.ivan.api.content.bean.mongo.HBArticle;
import cn.dendarii.ivan.common.dao.l3.BaseMongoDao;

@Repository("articleDao")
public class ArticleDao extends BaseMongoDao<HBArticle> {

}
