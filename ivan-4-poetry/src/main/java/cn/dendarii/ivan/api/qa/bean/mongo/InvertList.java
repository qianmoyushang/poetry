package cn.dendarii.ivan.api.qa.bean.mongo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import cn.dendarii.ivan.common.bean.mongo.BaseMgBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 倒排对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "hb_invert_list")
public class InvertList extends BaseMgBean<InvertList> implements Serializable {
    private static final long serialVersionUID = -5218141724377375345L;
    @Id
    private String id; // 文章的id号
    private List<String> articles;// 词对应的文章id号
    private List<Double> weight;// 词对应的文章的权重
}
