package cn.dendarii.ivan.api.qa.bean.http;

import lombok.Data;

/**
 * 返回给用户的答案
 */
@Data
public class PoetryAnswer {
    private String id;
    private String avatar;
    private String title;
    private String answer;
}
