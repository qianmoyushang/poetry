package cn.dendarii.ivan.api.content.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import cn.dendarii.ivan.api.content.bean.http.HBArticleForList;
import cn.dendarii.ivan.api.content.bean.http.RecArticleList;
import cn.dendarii.ivan.api.content.bean.mongo.HBArticle;
import cn.dendarii.ivan.api.content.service.ArticleService;
import cn.dendarii.ivan.api.user.bean.http.HBUserBasic;
import cn.dendarii.ivan.api.web.controller.BaseCRUDController;
import cn.dendarii.ivan.common.bean.anno.RegisterMethod;
import cn.dendarii.ivan.common.bean.http.ResponseBean;
import cn.dendarii.ivan.common.bean.mongo.BaseMgBean;
import cn.dendarii.ivan.common.service.BaseCRUDService;
import cn.dendarii.ivan.util.id.IDUtil;
import cn.dendarii.ivan.util.set.HBCollectionUtil;
import cn.dendarii.ivan.util.set.HBStringUtil;

@RestController
@RequestMapping(value = { "/${api.version}/b/article" })
@RegisterMethod(methods = { "get", "remove" })
public class ArticleBController extends BaseCRUDController<HBArticle> {
    @Autowired
    private ArticleService articleService;

    @Override
    protected BaseCRUDService<HBArticle> getService() {
        return articleService;
    }

    @Override
    @RequestMapping(value = "/{func}", method = { RequestMethod.GET })
    public ResponseBean man(@PathVariable String func) {
        return super.man(func);
    }

    @RequestMapping(value = "/update", method = { RequestMethod.POST })
    public ResponseBean update(@RequestBody HBArticle object) {
        return super.update(object);
    }

    @Override
    protected HBArticle prepareInsert(HBArticle object,
                                      ResponseBean responseBean) {
        String userid = getUseridFromRequest();
        HBUserBasic user = new HBUserBasic(userid != null ? userid
                : mainServer.conf().getAnonymousUser());
        object.setPublisher(user);
        return super.prepareInsert(object, responseBean);
    }

    @RequestMapping(value = "", method = { RequestMethod.PUT})
    public ResponseBean insert(@RequestBody HBArticle object) {
//        System.out.println("sssssss==="+object.toString());
        return super.insert(object);
    }

    /**
     * 假删除
     */
    @RegisterMethod
    public String fakedel() {
        String id = request.getParameter("id");
        if (HBStringUtil.isNotBlank(id)) {
            if (articleService.dao().updateOne(id, HBCollectionUtil.arrayToMap("state", 3))) {
                return "已将古诗移至回收站";
            }
        }
        return "删除古诗失败";
    }

    @Override
    protected List<String> prepareQueryFields(HBArticle object,
                                              ResponseBean responseBean) {
        if (object.getState() == -1) {
            object.setState(null);
        }
        List<String> fields = BaseMgBean.generateFieldListToList(HBArticleForList.class);
        return fields;
    }

    @RequestMapping(value = "/query", method = { RequestMethod.POST })
    public ResponseBean query(@RequestBody HBArticle object) {
        return super.query(object);
    }

    /*获取古诗列表信息*/
    @RegisterMethod
    public RecArticleList getRecList() {
        String keyword = request.getParameter("keyword");
        Query query = Query.query(Criteria.where("title").regex(keyword));
        query.limit(30);
        List<HBArticle> list = mongoTemplate.find(query, HBArticle.class);
        RecArticleList reclist = new RecArticleList();
        reclist.setRecid(IDUtil.generateASpliceId());
        reclist.setList(list);
        return reclist;
    }

    /*批量上传古诗*/
//    @RequestMapping(value = "/import", method = { RequestMethod.POST })
    @RegisterMethod
    public String importPoetry(){

        System.out.println("===批量上传古诗====");
        return "";
    }

}
