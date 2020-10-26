package cn.dendarii.ivan.api.reply.service;

import cn.dendarii.ivan.api.reply.bean.HBReply;
import cn.dendarii.ivan.api.reply.dao.HBReplyDao;
import cn.dendarii.ivan.common.dao.l2.BaseCRUDDao;
import cn.dendarii.ivan.common.service.BaseCRUDService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class HBReplyService extends BaseCRUDService<HBReply> {

    @Resource
    private HBReplyDao hbReplyDao;

    public BaseCRUDDao<HBReply> dao() {
        return hbReplyDao;
    }
}
