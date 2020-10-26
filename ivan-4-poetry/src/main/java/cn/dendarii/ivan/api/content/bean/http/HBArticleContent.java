package cn.dendarii.ivan.api.content.bean.http;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import cn.dendarii.ivan.common.bean.mongo.BaseMgBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 列表展示用，文章正文
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "hb_articles")
public class HBArticleContent extends BaseMgBean<HBArticleContent> implements Serializable {
    private static final long serialVersionUID = 2825125656077687007L;
    @Id
    private String id; // 文章的id号
    private String title; // 文章标题
    private String content; // 文章内容
    private String author; // 古诗作者

}
