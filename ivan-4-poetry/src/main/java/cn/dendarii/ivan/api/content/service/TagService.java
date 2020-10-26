package cn.dendarii.ivan.api.content.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dendarii.ivan.api.content.bean.mongo.HBTag;
import cn.dendarii.ivan.api.content.dao.TagDao;
import cn.dendarii.ivan.common.dao.l2.BaseCRUDDao;
import cn.dendarii.ivan.common.service.BaseCRUDService;

@Service
public class TagService extends BaseCRUDService<HBTag> {
    @Resource
    private TagDao tagDao;

    @Override
    public BaseCRUDDao<HBTag> dao() {
        return tagDao;
    }

    public Collection<HBTag> findAllTagByParent(String parent) {
        return tagDao.findAll("parent", parent);
    }

    /**
     * 从parent节点开始，获取N层的节点
     */
    public Collection<HBTag> findRecrusiveTagByParent(String parent,
                                                      int N) {
        // 1.获取第一层节点
        Map<String, Object> mongoMap = new HashMap<>();
        mongoMap.put("parent", parent);
        Collection<HBTag> roots = dao().query(mongoMap);
        // 2.递归获取root下第一层的节点
        if (roots != null && N > 0) {
            for (HBTag root : roots) {
                root.setChildren(findRecrusiveTagByParent(root.getId(), N - 1));
            }
        }
        return roots;
    }

    public boolean recursiveDelete(String id) {
        return tagDao.recursiveDelete(id, 4);
    }

    public Map<String, HBTag> getAllTree() {
        return tagDao.getAllTree();
    }

    public boolean updateId(HBTag object) {
        return tagDao.updateId(object);
    }

}
