package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

/**
 * @Author GuoDingWei
 * @Date 2022/6/14 15:23
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    //依赖注入
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;

    //数据查询
    //提供id，姓名，邮件来查询用户
    @Test
    public void selectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user= userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    //插入数据
    @Test
    public void insertUser(){
        User user = new User();
        user.setUsername("111");
        user.setPassword("123222456");
        user.setSalt("003330");
        user.setEmail("2q44422323q.com");
        user.setType(1);
        user.setStatus(1);
        user.setActivationCode("132323");
        user.setHeaderUrl("423256");
        user.setCreateTime(new Date());
        int i = userMapper.insertUser(user);
        System.out.println(i);
    }

    //更新数据
    @Test
    public void updateUser(){
        int i = userMapper.updateStatus(137, 1);
        int i1 = userMapper.updateHeader(137, "9090902");
        int i2 = userMapper.updatePassword(137, "65432221");
        System.out.println(i+":"+i1+":"+i2);
    }

    //首页模块dao的测试
    @Test
    public void testSelectDiscuss(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(149, 0, 10,0);
        for(int i = 0;i < discussPosts.size();i++){
            System.out.println(discussPosts.get(i));
        }

        int i = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(i);
    }

    @Test
    public void testLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 100 * 60 * 10));
        int i = loginTicketMapper.insertLoginTicket(loginTicket);
        System.out.println(i);
        LoginTicket abc = loginTicketMapper.selectByTicket("abc");
        System.out.println(abc);
        int abc1 = loginTicketMapper.updateStatus("abc", 0);
        System.out.println(abc1);
    }

    @Test
    public void testSelectLetters() {
        List<Message> list = messageMapper.selectConversations(111, 0, 20);
        for (Message message : list) {
            System.out.println(message);
        }

        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        list = messageMapper.selectLetters("111_112", 0, 10);
        for (Message message : list) {
            System.out.println(message);
        }

        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);

        //count = messageMapper.selectLetterUnreadCount(131, "111_131");
        //System.out.println(count);
    }
}