package cn.dendarii.ivan.api.content.bean.mongo;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import cn.dendarii.ivan.common.bean.mongo.BaseTreeMgBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标签
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "hb_tags")
public class HBTag extends BaseTreeMgBean<HBTag> implements Serializable {
    private static final long serialVersionUID = 7611516976400146398L;
    @Id
    private String id; // 标签的名称
    private Double w; // 标签的权重

    public HBTag() {}

    public HBTag(String id) {
        this.id = id;
    }
}
