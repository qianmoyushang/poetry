package cn.dendarii.ivan.api.content.dao;

import org.springframework.stereotype.Repository;

import cn.dendarii.ivan.api.content.bean.mongo.HBTag;
import cn.dendarii.ivan.common.dao.l4.BaseTreeLocalMongoCacheDao;

@Repository("tagDao")
public class TagDao extends BaseTreeLocalMongoCacheDao<HBTag> {
}
