package cn.dendarii.ivan.api.user.dao;

import org.springframework.stereotype.Repository;

import cn.dendarii.ivan.api.user.bean.mongo.HBRole;
import cn.dendarii.ivan.common.dao.l3.BaseLocalMongoCacheDao;

@Repository("roleDao")
public class RoleDao extends BaseLocalMongoCacheDao<HBRole> {
}
