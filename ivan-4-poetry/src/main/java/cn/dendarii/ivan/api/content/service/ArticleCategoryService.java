package cn.dendarii.ivan.api.content.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dendarii.ivan.api.content.bean.mongo.HBArticleCategory;
import cn.dendarii.ivan.api.content.dao.ArticleCategoryDao;
import cn.dendarii.ivan.common.dao.l2.BaseCRUDDao;
import cn.dendarii.ivan.common.service.BaseCRUDService;
import cn.dendarii.ivan.util.set.HBCollectionUtil;

@Service
public class ArticleCategoryService extends BaseCRUDService<HBArticleCategory> {
    @Resource
    private ArticleCategoryDao articleCategoryDao;

    @Override
    public BaseCRUDDao<HBArticleCategory> dao() {
        return articleCategoryDao;
    }

    public Map<String, HBArticleCategory> findInitResources() {
        Collection<HBArticleCategory> allData = articleCategoryDao.findAll();
        if (HBCollectionUtil.isNotEmpty(allData)) {
            return allData.stream()
                          .collect(Collectors.toMap(HBArticleCategory::getId, Function.identity()));
        } else {
            return new HashMap<>();
        }
    }

    public HBArticleCategory getATreedCategory(String id) {
        return articleCategoryDao.getBeanInTree(id);
    }

    public Map<String, HBArticleCategory> getAllTree() {
        return articleCategoryDao.getAllTree();
    }

    public boolean recursiveDelete(String id) {
        return articleCategoryDao.recursiveDelete(id, 4);
    }

    public boolean updateId(HBArticleCategory object) {
        return articleCategoryDao.updateId(object);
    }
}