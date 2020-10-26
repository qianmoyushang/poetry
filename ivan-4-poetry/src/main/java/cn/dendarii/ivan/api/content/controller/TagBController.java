package cn.dendarii.ivan.api.content.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.dendarii.ivan.api.content.bean.mongo.HBTag;
import cn.dendarii.ivan.api.content.service.TagService;
import cn.dendarii.ivan.api.web.controller.BaseCRUDController;
import cn.dendarii.ivan.common.bean.anno.RegisterMethod;
import cn.dendarii.ivan.common.bean.http.ResponseBean;
import cn.dendarii.ivan.common.service.BaseCRUDService;
import cn.dendarii.ivan.util.set.HBStringUtil;

/**
 * 标签类
 */
@RestController
@RequestMapping(value = { "/${api.version}/b/tag" })
@RegisterMethod(methods = { "get", "remove" })
public class TagBController extends BaseCRUDController<HBTag> {
    @Autowired
    private TagService tagService;

    @Override
    protected BaseCRUDService<HBTag> getService() {
        return tagService;
    }

    @Override
    @RequestMapping(value = "/{func}", method = { RequestMethod.GET })
    public ResponseBean man(@PathVariable String func) {
        return super.man(func);
    }

    @RequestMapping(value = "/upsert", method = { RequestMethod.POST })
    public ResponseBean upsert(@RequestBody HBTag object) {
        if (HBStringUtil.isNotBlank(object.getOldId())) {
            tagService.updateId(object);
        }
        // 再修改其它的
        return super.upsert(object);
    }

    /**
     * 根据parent查询所有子类标签
     */
    @RegisterMethod(name = "parent")
    public Collection<HBTag> getTagsByParent() {
        String parent = request.getParameter("parent");
        if (HBStringUtil.isNotBlank(parent)) {
            return tagService.findAllTagByParent(parent);
        }
        return new ArrayList<HBTag>(1);
    }

    @RequestMapping(value = "", method = { RequestMethod.PUT })
    public ResponseBean insert(@RequestBody HBTag object) {
        return super.insert(object);
    }

    /**
     * 添加多个标签，按照-进行区分
     */
    @RequestMapping(value = "", method = { RequestMethod.POST })
    public ResponseBean addTags(@RequestBody HBTag tagParam) {
        ResponseBean responseBean = getReturn();
        if (tagParam.getId().indexOf("-") != -1) {
            String[] tagArray = tagParam.getId().split("-");
            for (String tagName : tagArray) {
                HBTag tag = new HBTag();
                tag.setParent(tagParam.getParent());
                tag.setId(tagName);
                tagService.dao().insert(tag);
            }
        } else {
            tagService.dao().insert(tagParam);
        }
        return returnBean(responseBean);
    }

    /**
     * 删除这个标签以及这个标签的所有子分类
     */
    @RegisterMethod(name = "delrecursive")
    public String delrecursive() {
        String id = request.getParameter("id");
        if (HBStringUtil.isNotBlank(id)) {
            if (tagService.recursiveDelete(id)) {
                return "删除成功";
            }
        }
        return "删除失败";
    }

    /**
     * 获取后台初始化系统需要用到的标签 默认获取2层的标签，因为数据过多，所以进行截断
     */
    @RegisterMethod(name = "init")
    public Map<String, HBTag> getInitDatas() {
        Map<String, HBTag> allTags = tagService.getAllTree();
        Map<String, HBTag> rsMap = new HashMap<String, HBTag>();
        int countSR = 0;
        int countYL = 0;
        for (Entry<String, HBTag> tagEntry : allTags.entrySet()) {
            if ("诗人".equals(tagEntry.getValue().getParent()) && ++countSR > 10) {
                continue;
            } else if ("韵律".equals(tagEntry.getValue().getParent()) && ++countYL > 10) {
                continue;
            } else {
                rsMap.put(tagEntry.getKey(), tagEntry.getValue());
            }
        }
//        rsMap.put("root", new HBTag("root"));
        return rsMap;
    }

}
