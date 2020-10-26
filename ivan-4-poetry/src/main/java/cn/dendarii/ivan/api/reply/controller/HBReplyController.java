package cn.dendarii.ivan.api.reply.controller;

import cn.dendarii.ivan.api.reply.bean.HBReply;
import cn.dendarii.ivan.api.reply.service.HBReplyService;
import cn.dendarii.ivan.api.web.controller.BaseCRUDController;
import cn.dendarii.ivan.api.web.controller.BaseController;
import cn.dendarii.ivan.common.bean.anno.RegisterMethod;
import cn.dendarii.ivan.common.bean.http.ResponseBean;
import cn.dendarii.ivan.common.service.BaseCRUDService;
import cn.dendarii.ivan.util.id.IDUtil;
import cn.dendarii.ivan.util.set.HBStringUtil;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(value = {"/${api.version}/b/hb_reply"})
@RegisterMethod(methods = { "get", "remove"})
public class HBReplyController extends BaseController {
    @Autowired
    private HBReplyService hbReplyService;

    private String url = "http://127.0.0.1:5050/poetry/";

    @Override
    @RequestMapping(value = "/{func}", method = {RequestMethod.GET})
    public ResponseBean man(@PathVariable String func){
        return super.man(func);
    }

    //关键字随机生成古诗
    @RegisterMethod
    public HBReply random_poetry(){
        HBReply reply = new HBReply();
        String ask = request.getParameter("ask");
        reply.setId(IDUtil.generateRandomKey());
        RestUtil restUtil = new RestUtil();
        try {
            if (HBStringUtil.isNotBlank(ask)){
                reply.setText(restUtil.doGetJson(url + "random/" + ask));
//                System.out.println("random_poetry======"+restUtil.doGetJson(url + "random"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reply;
    }

    ////生成藏头古诗
    @RegisterMethod
    public HBReply head_poetry(){
        HBReply reply = new HBReply();
        String ask = request.getParameter("ask");
        reply.setId(IDUtil.generateRandomKey());
        RestUtil restUtil = new RestUtil();
        try {
            if (HBStringUtil.isNotBlank(ask)){
                reply.setText(restUtil.doGetJson(url + "head_random/" + ask));
//                System.out.println("head_poetry======"+restUtil.doGetJson(url + ask));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reply;
    }



}
