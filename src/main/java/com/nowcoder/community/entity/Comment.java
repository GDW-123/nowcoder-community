package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @Author GuoDingWei
 * @Date 2022/6/19 8:50
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment {

    private int id;
    //用户id
    private int userId;
    //评论实体目标的类型，其中1代表帖子，2代表评论，3代表用户，4代表题目等等等
    private int entityType;
    //评论的实体id，就是帖子的id，是对哪个帖子进行评论
    private int entityId;
    //评论目标的id，就是说是对第一条评论进行评论，还是对具体的某一条的评论进行评论
    private int targetId;
    //评论的内容
    private String content;
    //评论的状态
    private int status;
    //评论的创建时间
    private Date createTime;
}
