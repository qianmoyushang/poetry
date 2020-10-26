package cn.dendarii.ivan.api.qa.dao;

import org.springframework.stereotype.Repository;

import cn.dendarii.ivan.api.qa.bean.mongo.InvertList;
import cn.dendarii.ivan.common.dao.l3.BaseMongoDao;

@Repository("invertListDao")
public class InvertListDao extends BaseMongoDao<InvertList> {

}
