package com.nowcoder.community.service;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author GuoDingWei
 * @Date 2022/6/19 16:19
 */
@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * 查询当前用户的会话列表,针对每个会话只返回一条最新的私信
     * @param userId 用户id
     * @param offset 偏移量
     * @param limit 每页显示的条数
     * @return 返回私信对象组成的集合
     */
    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    /**
     * 查询当前用户的会话数量
     * @param userId 用户的id
     * @return 返回会话的条数
     */
    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    /**
     * 查询某个会话所包含的私信列表
     * @param conversationId 会话的id
     * @param offset 偏移量
     * @param limit 每页显示的条数
     * @return 返回私信对象组成的集合
     */
    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    /**
     * 查询某个会话所包含的私信数量
     * @param conversationId 会话id
     * @return 返回私信的数量
     */
    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    /**
     * 查询未读私信的数量
     * @param userId 用户id
     * @param conversationId 会话id
     * @return 返回未读私信的数量
     */
    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    /**
     * 添加消息，即发送私信
     * @param message 消息对象
     * @return 返回成功条数
     */
    public int addMessage(Message message) {
        //防止转义字符
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        //敏感词的过滤
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    /**
     * 获取消息，将消息从未读的状态改成已读的形式
     * @param ids 消息
     * @return 成功条数
     */
    public int readMessage(List<Integer> ids) {
        //将状态改为1表示已读的形式
        return messageMapper.updateStatus(ids, 1);
    }

    /**
     * 查询某个主题下最新的通知
     * @param userId 用户id
     * @param topic 主题
     * @return 该主题下的消息对象
     */
    public Message findLatestNotice(int userId, String topic) {
        return messageMapper.selectLatestNotice(userId, topic);
    }

    /**
     * 查询某个主题所包含的通知数量
     * @param userId 用户id
     * @param topic 主题
     * @return 该主题下的消息数量
     */
    public int findNoticeCount(int userId, String topic) {
        return messageMapper.selectNoticeCount(userId, topic);
    }

    /**
     * 查询未读的通知的数量
     * @param userId 用户id
     * @param topic 主题
     * @return 该主题下未读消息数量
     */
    public int findNoticeUnreadCount(int userId, String topic) {
        return messageMapper.selectNoticeUnreadCount(userId, topic);
    }

    /**
     * 查询某个主题所包含的通知列表
     * @param userId 用户id
     * @param topic 主题
     * @param offset 起始页
     * @param limit 每页显示的数量
     * @return
     */
    public List<Message> findNotices(int userId, String topic, int offset, int limit) {
        return messageMapper.selectNotices(userId, topic, offset, limit);
    }
}
