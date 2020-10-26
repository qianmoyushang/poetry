package cn.dendarii.ivan.api.user.dao;

import org.springframework.stereotype.Repository;

import cn.dendarii.ivan.api.user.bean.mongo.HBUserGroup;
import cn.dendarii.ivan.common.dao.l3.BaseLocalMongoCacheDao;

@Repository("userGroupDao")
public class UserGroupDao extends BaseLocalMongoCacheDao<HBUserGroup> {
}
