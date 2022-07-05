package com.nowcoder.community.service;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

import static com.nowcoder.community.util.CommunityConstant.ENTITY_TYPE_POST;

/**
 * @Author GuoDingWei
 * @Date 2022/6/19 9:21
 */

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

    /**
     * 根据实体类型查询评论
     * @param entityType 实体类型
     * @param entityId 实体id
     * @param offset 起始值
     * @param limit 每页显示的条数
     * @return 返回评论对象组成的集合
     */
    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    /**
     * 根据实体类查询评论数，用于分页
     * @param entityType 实体类型
     * @param entityId 实体id
     * @return 返回查询到的条数
     */
    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    /**
     * 添加评论
     * 因为在这里是涉及到两个操作的，
     * 第一个是添加评论，第二个是修改评论的数量，
     * 因此是需要使用到事务的
     * @param comment 评论对象
     * @return 返回添加评论成功的条数
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        // 添加评论
        // 因为在添加评论的时候和发布内容的时候是一样的，需要避免转义字符和敏感词汇
        // 因此需要对评论的内容进行相应的处理
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        //数据的插入
        int rows = commentMapper.insertComment(comment);

        // 更新帖子评论数量,这里需要判断这条评论是对帖子的评论，而不是对评论的评论的
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            //通过实体类来查询帖子的评论的数量
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            //然后将该评论的数量进行更新
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }
        return rows;
    }

    public Comment findCommentById(int id) {
        return commentMapper.selectCommentById(id);
    }
}