package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author GuoDingWei
 * @Date 2022/6/19 9:12
 */
@Mapper
public interface CommentMapper {

    /**
     * 根据实体类来查询评论，是查询帖子的评论还是评论的评论，
     * 就使用这个实体类型来进行区别，同时还要实现分页的功能
     * @param entityType 实体类型
     * @param entityId 实体类id
     * @param offset 偏移量，用于分页
     * @param limit 每页显示的数量
     * @return 返回查询的评论对象组成的集合
     */
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    /**
     * 根据实体类来查询评论的数量，用于后序的分页的功能
     * @param entityType 实体类型
     * @param entityId 实体类id
     * @return 返回查询的评论的数量
     */
    int selectCountByEntity(int entityType, int entityId);

    /**
     * 添加评论
     * @param comment 评论对象
     * @return 返回添加成功的条数
     */
    int insertComment(Comment comment);


    /**
     * 根据id查询评论
     * @param id id
     * @return
     */
    Comment selectCommentById(int id);


}
