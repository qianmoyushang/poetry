package cn.dendarii.ivan.api.reply.controller;

import cn.dendarii.ivan.api.content.bean.http.HBArticleContent;
import cn.dendarii.ivan.api.qa.bean.http.PoetryAnswer;
import cn.dendarii.ivan.api.qa.service.QaService;
import cn.dendarii.ivan.api.web.controller.BaseController;
import cn.dendarii.ivan.common.bean.anno.RegisterMethod;
import cn.dendarii.ivan.common.bean.http.ResponseBean;
import cn.dendarii.ivan.util.id.IDUtil;
import cn.dendarii.ivan.util.set.HBCollectionUtil;
import cn.dendarii.ivan.util.set.HBStringUtil;
import cn.dendarii.ivan.util.set.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = { "/${api.version}/b/reply" })
public class ReplyBController extends BaseController {
    @Autowired
    private QaService qaService;

    @Override
    @RequestMapping(value = "/{func}", method = { RequestMethod.GET })
    public ResponseBean man(@PathVariable String func) {
        return super.man(func);
    }

    @RegisterMethod
    public PoetryAnswer random() {
        PoetryAnswer answer = new PoetryAnswer();
        String ask = request.getParameter("ask");
        if (HBStringUtil.isNotBlank(ask)) {
            ask = ask.trim();
            answer.setId(IDUtil.generateRandomKey());
            answer.setAvatar("https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar");
            answer.setTitle("机器人说：");

            String[] randomReply = {"你谁啊？", "请你再说一遍！", "想和我聊天？得先夸我！", "我不知道你在讲什么。。。", "不好意思，我不想和你说话。",
                    "先告诉我你是谁。", "竖子不足以谋也！", "我选择沉默", "来吧，一起吹牛逼。。", "我很困，不想聊天", "别废话，先给我讲个笑话", "你从哪里来",
                    "心情不好，最好别搭理我", "等我忙完再回复你", "敢问尊姓大名", "近来可好？", "看来你是想和我聊天", "你是要请我吃饭吗？",
                    "先给我一个让我回复你的理由", "哈哈哈"};
            answer.setAnswer(randomReply[(int)(Math.random()*19)]);

        }
        return answer;
    }


}
