package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @Author GuoDingWei
 * @Date 2022/6/14 15:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
//在 7.0 以及之后的版本中 Type 被废弃了。一个 index 中只有一个默认的 type，即 _doc。因此可以不写
@Document(indexName = "discusspost",shards = 6,replicas = 3)
public class DiscussPost {

    //数据库的id
    @Id
    private int id;
    //用户的id
    @Field(type = FieldType.Integer)
    private int userId;
    //帖子的标题
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String title;
    //帖子的内容
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String content;
    //帖子的类型，1表示置顶
    @Field(type = FieldType.Integer)
    private int type;
    //帖子的状态，0表示正常，1表示精华，2表示拉黑
    @Field(type = FieldType.Integer)
    private int status;
    //帖子创建的日期
    @Field(type = FieldType.Date)
    private Date createTime;
    //帖子评论的数量
    @Field(type = FieldType.Integer)
    private int commentCount;
    //帖子的分数
    @Field(type = FieldType.Double)
    private double score;

}
