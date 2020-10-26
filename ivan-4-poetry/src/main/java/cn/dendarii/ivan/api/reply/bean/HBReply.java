package cn.dendarii.ivan.api.reply.bean;

import cn.dendarii.ivan.common.bean.mongo.BaseMgBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "reply")
public class HBReply extends BaseMgBean<HBReply> implements Serializable{
    private static final long serialVersionUID = 9187104626010481847L;
    @Id
    private String id;
    private String text;



}
