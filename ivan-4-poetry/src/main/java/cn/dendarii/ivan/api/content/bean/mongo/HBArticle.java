package cn.dendarii.ivan.api.content.bean.mongo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import cn.dendarii.ivan.api.content.bean.enums.ArticleStateEnum;
import cn.dendarii.ivan.api.user.bean.http.HBUserBasic;
import cn.dendarii.ivan.common.bean.mongo.BaseMgBean;
import cn.dendarii.ivan.util.id.IDUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "hb_articles")
public class HBArticle extends BaseMgBean<HBArticle> implements Serializable {
    private static final long serialVersionUID = -9087214526010481847L;
    @Id
    private String id; // 文章的id号
    private String title; // 文章标题
    private String content; // 文章内容
    private String tone; // 韵律
    private String author; // 古诗作者
    private Integer state; // 文章状态
    private Date createTime; // 文章上传日期

    @Indexed
    private List<String> categorys; // 文章分类
    @Indexed
    private List<String> tags; // 标签
    @DBRef
    private HBUserBasic publisher;// 发布文章作者

    @Override
    public void prepareHBBean() {
        id = id == null ? IDUtil.generateTimedIDStr() : id;
        title = title != null ? title : "未命名";
        // 如果文章新产生的时候就发布，那么发布时间和创建时间相等
        createTime = createTime != null ? createTime : new Date();
        state = state != null ? state : ArticleStateEnum.DRAFT.getIndex();
    }
}
