package cn.dendarii.ivan.api.content.dao;

import org.springframework.stereotype.Repository;

import cn.dendarii.ivan.api.content.bean.mongo.HBArticleCategory;
import cn.dendarii.ivan.common.dao.l4.BaseTreeLocalMongoCacheDao;

/**
 * 文章分类少且小，直接全量加载到内存即可
 */
@Repository("articleCategoryDao")
public class ArticleCategoryDao extends BaseTreeLocalMongoCacheDao<HBArticleCategory> {
}
