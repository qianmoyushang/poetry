package cn.dendarii.ivan.api.content.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.dendarii.ivan.api.content.bean.mongo.HBArticleCategory;
import cn.dendarii.ivan.api.content.service.ArticleCategoryService;
import cn.dendarii.ivan.api.web.controller.BaseCRUDController;
import cn.dendarii.ivan.common.bean.anno.RegisterMethod;
import cn.dendarii.ivan.common.bean.http.ResponseBean;
import cn.dendarii.ivan.common.service.BaseCRUDService;
import cn.dendarii.ivan.util.set.HBStringUtil;

@RestController
@RequestMapping(value = { "/${api.version}/b/articlecategory" })
@RegisterMethod(methods = { "get", "remove" })
public class ArticleBCategoryController extends BaseCRUDController<HBArticleCategory> {
    @Autowired
    private ArticleCategoryService articleCategoryService;

    @Override
    protected BaseCRUDService<HBArticleCategory> getService() {
        return articleCategoryService;
    }

    @Override
    @RequestMapping(value = "/{func}", method = { RequestMethod.GET })
    public ResponseBean man(@PathVariable String func) {
        return super.man(func);
    }

    @RequestMapping(value = "/query", method = { RequestMethod.POST })
    public ResponseBean query(@RequestBody HBArticleCategory object) {
        return super.query(object);
    }

    @RequestMapping(value = "/update", method = { RequestMethod.POST })
    public ResponseBean update(@RequestBody HBArticleCategory object) {
        if (HBStringUtil.isNotBlank(object.getOldId())) {
            articleCategoryService.updateId(object);
        }
        // 再修改其它的
        return super.update(object);
    }

    @RequestMapping(value = "", method = { RequestMethod.PUT })
    public ResponseBean insert(@RequestBody HBArticleCategory object) {
        return super.insert(object);
    }

    /**
     * 删除这个分类以及这个分类的所有子分类
     */
    @RegisterMethod(name = "delrecursive")
    public String delrecursive() {
        String id = request.getParameter("id");
        if (HBStringUtil.isNotBlank(id)) {
            if (articleCategoryService.recursiveDelete(id)) {
                return "删除成功";
            }
        }
        return "删除失败";
    }

    /**
     * 获取后台初始化系统需要用到的分类
     */
    @RegisterMethod(name = "init")
    public Map<String, HBArticleCategory> getInitDatas() {
        return articleCategoryService.getAllTree();
    }
}
