package cn.dendarii.ivan.api.user.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.dendarii.ivan.api.user.bean.mongo.HBRole;
import cn.dendarii.ivan.api.user.service.RoleService;
import cn.dendarii.ivan.api.web.controller.BaseCRUDController;
import cn.dendarii.ivan.common.bean.anno.RegisterMethod;
import cn.dendarii.ivan.common.bean.http.ResponseBean;
import cn.dendarii.ivan.common.service.BaseCRUDService;

/**
 * 角色管理
 */
@RestController
@RequestMapping(value = "/${api.version}/b/role")
@RegisterMethod(methods = { "get", "remove" })
public class RoleBController extends BaseCRUDController<HBRole> {
    @Autowired
    private RoleService roleService;

    @Override
    protected BaseCRUDService<HBRole> getService() {
        return roleService;
    }

    @RequestMapping(value = "/{func}", method = { RequestMethod.GET })
    public ResponseBean man(@PathVariable String func) {
        return super.man(func);
    }

    @RequestMapping(value = "", method = { RequestMethod.PUT })
    public ResponseBean insert(@RequestBody HBRole object) {
        return super.insert(object);
    }

    @RequestMapping(value = "/update", method = { RequestMethod.POST })
    public ResponseBean update(@RequestBody HBRole object) {
        return super.update(object);
    }

    @RequestMapping(value = "/query", method = { RequestMethod.POST })
    public ResponseBean query(@RequestBody HBRole object) {
        return super.query(object);
    }

    /**
     * 获取后台初始化系统需要用到的信息
     */
    @RegisterMethod(name = "init")
    public Collection<HBRole> getInitDatas() {
        return roleService.dao().findAll();
    }
}
