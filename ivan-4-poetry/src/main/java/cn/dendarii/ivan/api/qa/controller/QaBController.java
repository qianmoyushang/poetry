package cn.dendarii.ivan.api.qa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.TextScore;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequestMapping(value = { "/${api.version}/b/qa" })
public class QaBController extends BaseController {
    @Autowired
    private QaService qaService;

    @Override
    @RequestMapping(value = "/{func}", method = { RequestMethod.GET })
    public ResponseBean man(@PathVariable String func) {
        return super.man(func);
    }

    @RegisterMethod
    public PoetryAnswer poetry() {
        PoetryAnswer answer = new PoetryAnswer();
        String ask = request.getParameter("ask");
        if (HBStringUtil.isNotBlank(ask)) {
            ask = ask.trim();
            answer.setId(IDUtil.generateRandomKey());
            answer.setAvatar("https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar");
            answer.setTitle("机器人说：");
            if (ask.startsWith("来一首")) {
                if (ask.endsWith("的诗")) {
                        String author = ask.substring(3, ask.length() - 2);
                    System.out.println(author);
                    List<HBArticleContent> poetries = mongoTemplate.find(Query.query(Criteria.where("author")
                                                                                             .is(author)),
                                                                         HBArticleContent.class);
                    if (HBCollectionUtil.isNotEmpty(poetries)) {
                        HBArticleContent content = RandomUtils.randomItem(poetries);
                        answer.setAnswer("《" + content.getTitle() + "》\\n" + content.getContent());
                    }
                } else if (ask.startsWith("来一首诗，")) {
                    String[] toks = ask.split("要有");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < toks.length; i++) {
                        sb.append(toks[i]);
                        System.out.println(toks[i]);
                    }
                    List<HBArticleContent> poetries = mongoTemplate.find(Query.query(Criteria.where("content")
                                                                                             .regex("["
                                                                                                     + sb.toString()
                                                                                                     + "]")),
                                                                         HBArticleContent.class);
                    if (HBCollectionUtil.isNotEmpty(poetries)) {
                        HBArticleContent content = RandomUtils.randomItem(poetries);
                        answer.setAnswer("《" + content.getTitle() + "》\\n" + content.getContent());
                    }
                } else {
                    String category = ask.substring(3);
                    System.out.println(category);
                    List<HBArticleContent> poetries = mongoTemplate.find(Query.query(Criteria.where("categorys")
                                                                                             .in(category)),
                                                                         HBArticleContent.class);
                    if (HBCollectionUtil.isNotEmpty(poetries)) {
                        HBArticleContent content = RandomUtils.randomItem(poetries);
                        answer.setAnswer("《" + content.getTitle() + "》\\n" + content.getContent());
                    }
                }
            }else {
                String[] s = { "我", "你", "他", "她", "看起来", "玩起来","杀起来" ,"很","拥有", "驾驶", "摧毁"
                        , "极度", "非常","汽车", "飞机", "大炮","刺激", "爽快", "开心", "李白"};
                String bev = "是";
                String[] c = { "富有的", "漂亮的", "美丽的", "伤心的", "快乐的", "有趣的", "好笑的","拥有", "驾驶", "摧毁"};
                String[] abstractn = { "刺激", "爽快", "开心" };
                int srandom = (int) (Math.random() * 20);
                int crandom = (int) (Math.random() * 10);
                int nrandom = (int) (Math.random() * 3);
                String str = s[srandom]+bev+c[crandom]+bev + s[srandom] + abstractn[nrandom];

//                String str="";
//                for(int i=0;i<10;i++){
//                    char c=(char)(0x4e00+(int) (Math.random()*(0x9fa5-0x4e00+1)));
//                    str += c;
//                }
                answer.setAnswer(str);
//                System.out.println("========="+str);
            }
        }
        return answer;
    }


}
