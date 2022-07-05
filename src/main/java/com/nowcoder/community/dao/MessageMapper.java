package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author GuoDingWei
 * @Date 2022/6/19 15:49
 */
@Mapper
public interface MessageMapper {

    /**
     * 查询当前用户的会话列表,针对每个会话只返回一条最新的私信
     * @param userId 用户id
     * @param offset 起始位置
     * @param limit 每页显示的条数
     * @return 返回私信列表组成的集合
     */
    List<Message> selectConversations(int userId, int offset, int limit);

    /**
     * 查询当前用户的会话数量
     * @param userId 用户id
     * @return 返回当前用户的会话数量，为了分页处理
     */
    int selectConversationCount(int userId);

    /**
     * 查询某个会话所包含的私信列表
     * @param conversationId 会话id
     * @param offset 起始位置
     * @param limit 每页显示的数量
     * @return 返回该会话中的私信的数量
     */
    List<Message> selectLetters(String conversationId, int offset, int limit);

    /**
     * 查询某个会话所包含的私信数量
     * @param conversationId 会话id
     * @return 返回当前会话的私信的数量，为了分页处理
     */
    int selectLetterCount(String conversationId);

    /**
     * 新增消息，即发布消息
     * @param message 消息对象
     * @return 返回新增消息的数量
     */
    int insertMessage(Message message);

    /**
     * 修改消息的状态，是已读还是未读
     * @param ids 消息（私信）的id
     * @param status 消息（私信）的状态
     * @return 返回修改成功的条数
     */
    int updateStatus(List<Integer> ids, int status);

    /**
     * 查询未读私信的数量
     * @param userId 用户id
     * @param conversationId 会话id
     * @return 返回查询未读私信的数量
     */
    int selectLetterUnreadCount(int userId, String conversationId);

    /**
     * 查询某个主题下最新的通知
     * @param userId 用户id
     * @param topic 主题
     * @return 通知对象
     */
    Message selectLatestNotice(int userId, String topic);

    /**
     * 查询某个主题所包含的通知数量
     * @param userId 用户id
     * @param topic 主题
     * @return 该主题下的通知的条数
     */
    int selectNoticeCount(int userId, String topic);

    /**
     * 查询未读的通知的数量
     * @param userId 用户id
     * @param topic 主题
     * @return 该主题下未读消息的数量
     */
    int selectNoticeUnreadCount(int userId, String topic);


    /**
     * 查询某个主题所包含的通知列表
     * @param userId 用户id
     * @param topic 主题（评论，赞，关注）
     * @param offset 起始页
     * @param limit 每页显示的数量
     * @return 通知消息对象组成的列表
     */
    List<Message> selectNotices(int userId, String topic, int offset, int limit);
}
