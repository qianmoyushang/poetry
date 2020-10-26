package cn.dendarii.ivan.api.user.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.dendarii.ivan.api.user.bean.enums.HBRoleEnum;
import cn.dendarii.ivan.api.user.bean.mongo.HBUser;
import cn.dendarii.ivan.api.user.dao.RoleDao;
import cn.dendarii.ivan.api.user.service.UserService;
import cn.dendarii.ivan.api.web.controller.BaseCRUDController;
import cn.dendarii.ivan.common.bean.enums.ApiCode;
import cn.dendarii.ivan.common.bean.http.ResponseBean;
import cn.dendarii.ivan.common.service.BaseCRUDService;

/**
 * 用户修改，正常用户修改，只能修改相关信息
 */
@RestController
@RequestMapping(value = "/${api.version}/b/user")
public class UserBController extends BaseCRUDController<HBUser> {
    @Autowired
    private UserService userService;
    @Resource
    public RoleDao roleDao;

    @Override
    protected BaseCRUDService<HBUser> getService() {
        return userService;
    }

    @Override
    protected HBUser prepareInsert(HBUser object,
                                   ResponseBean responseBean) {
        List<String> group = new ArrayList<>();
        if (object.getGroup() != null) {
            group = object.getGroup();
        }
        group.add(HBRoleEnum.ROLE_STAFF.getName());// 所有用户归为normal用户组
        if (object.getLastPasswordResetDate() != null) {
            // 这个如果不设置，解析jwttoken会出问题
            object.setLastPasswordResetDate(new Date());
        }
        // 最后进行一下重置
        object = userService.initNewHBUser(object.toMongoHashMap());
        object.setGroup(group);
        return super.prepareInsert(object, responseBean);
    }

    @RequestMapping(value = "", method = { RequestMethod.PUT })
    public ResponseBean insert(@RequestBody HBUser object) {
        System.out.println(object.toString());
        return super.insert(object);
    }

    /**
     * 用户只能更新自己的信息
     */
    @Override
    protected HBUser prepareUpdate(HBUser hbuser,
                                   ResponseBean responseBean) {
        String userid = getUseridFromRequest();
        if (StringUtils.isEmpty(userid)) {
            responseBean.setCodeEnum(ApiCode.NO_AUTH);
        }
        return super.prepareUpdate(hbuser, responseBean);
    }

    @RequestMapping(value = "/update", method = { RequestMethod.POST })
    public ResponseBean update(@RequestBody HBUser object) {
        return super.update(object);
    }

    @Override
    protected HBUser postInsert(HBUser object,
                                ResponseBean responseBean) {
        return super.prepareInsert(object, responseBean);
    }

    @Override
    protected String prepareRemove(String id,
                                   ResponseBean responseBean) {
        // 删除
        Map<String, Object> params = new HashMap<>();
        params.put("user", id);
        return super.prepareRemove(id, responseBean);
    }

    @RequestMapping(value = "/{id}", method = { RequestMethod.DELETE })
    public ResponseBean remove(@PathVariable("id") String id) {
//        System.out.println("===remove id ==" + id);
        return super.remove(id);
    }

}
