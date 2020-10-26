package cn.dendarii.ivan.api.reply.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

public class RestUtil {
    public static String doPostJson(String url, String json){
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("返回的数据"+resultString);
        return resultString;
    }

    public String doGetJson(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        System.out.println("======Get："+httpGet.getURI());
        CloseableHttpResponse response = null;
        String content = "";
        try{
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if(entity!=null) {
                JSONObject json = JSONObject.parseObject(EntityUtils.toString(entity,"utf-8"));
                content = json.getString("out_top") + ',' + json.getString("output") + '。';
                System.out.println("响应状态："+response.getStatusLine());
//                content = EntityUtils.toString(entity,"utf-8");
                System.out.println("响应内容："+content);
                System.out.println("内容长度："+content.length());
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(response != null){
                response.close();
            }
        }
        return content;
    }

    public static void main(String[] args) throws IOException {

//        json1.put("requestId","6c84fb9012c411e1840d7b25c5ee775a");
//        json1.put("reqType", "REQ");
//        json1.put("apiName","dryRun");
        String s = "海";
        String s1 = "海阔天空";
//        String url="http://127.0.0.1:5050/poetry/random/"+s;
        String url="http://127.0.0.1:5050/poetry/head_random/"+s1;
//        String ss = doGetJson(url);
//        System.out.println(doGetJson(url));

//        System.out.println(doPostJson(url,json1.toString()));
    }

}
