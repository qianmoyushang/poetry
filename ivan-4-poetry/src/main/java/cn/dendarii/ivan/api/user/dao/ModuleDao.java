package cn.dendarii.ivan.api.user.dao;

import org.springframework.stereotype.Repository;

import cn.dendarii.ivan.api.user.bean.mongo.HBModule;
import cn.dendarii.ivan.common.dao.l3.BaseLocalMongoCacheDao;

@Repository("moduleDao")
public class ModuleDao extends BaseLocalMongoCacheDao<HBModule> {
}
