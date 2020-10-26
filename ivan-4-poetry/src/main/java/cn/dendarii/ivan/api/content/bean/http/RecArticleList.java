package cn.dendarii.ivan.api.content.bean.http;

import java.util.List;

import cn.dendarii.ivan.api.content.bean.mongo.HBArticle;
import lombok.Data;

/**
 * 返回给用户的推荐结果
 */
@Data
public class RecArticleList {
    private String recid; // 本次推荐任务的id号
    private List<HBArticle> list;
}
