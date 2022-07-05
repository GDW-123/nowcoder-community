package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author GuoDingWei
 * @Date 2022/6/14 15:29
 */
@Mapper
public interface DiscussPostMapper {

    //下面的两个方法用于首页的显示

    /**
     * 第一个方法是用来做分页查询的
     * @param userId 用户的id，在首页的时候是用不到的，但是在后面的个人主页上面就会有我的帖子里面
     *               就需要使用到这个userId，因此我们在编写sql语句的时候就可以写一个条件判断，
     *               当我们步传入这个数据的时候，这个userId默认就是0，当为0的时候就表示显示全部的数据
     * @param offset 起始页面
     * @param limit 每页显示的条数
     * @param orderMode 排序规则
     * @return 返回值就是所有的帖子对象组成的列表
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit,int orderMode);

    /**
     * 第二个方法，用来计算所有的帖子的数量，用来实现分页的
     * @param userId 用户id，用来查询用户的信息，显示在首页上面的
     *               @Param 的作用是取别名，比如说我们在编写sql的时候这个字段比较长，
     *               这样就可以通过取别名的方式来让这个字段短一点
     *               另外，如果我们使用动态sql（如if条件中），并且该动态sql的参数只有一个，
     *               那么这一个参数一定要取别名的
     * @return 返回值就是帖子的条数
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    /**
     * 用于帖子的发布
     * @param discussPost 帖子对象
     * @return 返回发布帖子成功的条数
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 查询帖子的详细信息
     * @param id 帖子的id
     * @return 返回帖子对象
     */
    DiscussPost selectDiscussPostById(int id);

    /**
     * 更新帖子的评论数量
     * @param id 帖子的id
     * @param commentCount 这个帖子的评论数量
     * @return 返回修改成功的条数
     */
    int updateCommentCount(int id, int commentCount);

    /**
     * 修改帖子的类型，用于置顶操作
     * @param id 帖子的id
     * @param type 帖子的类型
     * @return
     */
    int updateType(int id, int type);

    /**
     * 修改帖子的状态，用于加精操作
     * @param id 帖子的id
     * @param status 帖子的状态
     * @return
     */
    int updateStatus(int id, int status);

    /**
     * 修改帖子的分数，便于最热帖子的排行
     * @param id 帖子的id
     * @param score 分数
     * @return
     */
    int updateScore(int id, double score);
}