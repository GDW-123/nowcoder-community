package com.nowcoder.community.controller;

import com.nowcoder.community.entity.*;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @Author GuoDingWei
 * @Date 2022/6/18 15:55
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 增加帖子，即发布帖子
     * @param title 在发布帖子里面有两个部分的内容，
     *              一个部分是标题，另一部分是内容
     * @param content 帖子的内容
     * @return 跳转到指定的页面
     */
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        //用户的信息放在了ThreadLocal中的
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登录哦!");
        }

        //在数据库中帖子是有很多的属性的，我们在前端传入的数据只有标题和内容，其他的都没有
        //但是其他的内容都差不多，因此我们可以在后台来进行手动传入参数。
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());//作者id
        post.setTitle(title);//帖子标题
        post.setContent(content);//帖子内容
        post.setCreateTime(new Date());//帖子的创建时间
        discussPostService.addDiscussPost(post);//增加帖子

        // 触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(user.getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(post.getId());
        eventProducer.fireEvent(event);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, post.getId());


        // 报错的情况,将来统一处理.
        return CommunityUtil.getJSONString(0, "发布成功!");
    }

    /**
     * 查询帖子的详情页面
     *      path = "/detail/{discussPostId}"中的帖子id会在前端
     *      点击标题进行跳转的时候得到这个帖子的id，从而来查询帖子的详情信息
     *      @PathVariable("discussPostId")就是为了获得前端传递过来的参数
     * 因为是向数据库查询信息，因此是get请求
     * @param discussPostId 帖子的id
     * @param model 传递给前端的模板
     * @param page 用于评论的分页查询
     * @return 跳转到帖子的详情页面
     */

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model,Page page) {
        // 根据点击帖子的标题得到的帖子的id来查询帖子对象
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        // 注意，这里的作者信息就不是从ThreadLocal中拿到的数据了，这不一定是登录的用户的帖子
        //而是通过帖子对象来查询用户的id，从而根据这个用户的id来查询用户对象
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        // 获取帖子的点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount", likeCount);
        // 点赞状态
        int likeStatus = hostHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus", likeStatus);

        // 评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());

        // 评论: 给帖子的评论
        // 回复: 给评论的评论
        // 查询帖子的评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(
                ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        // 评论VO列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if(commentList != null){
            for(Comment comment : commentList){
                // 评论VO,创建一个Map集合，用来存放评论列表的评论和作者信息
                Map<String, Object> commentVo = new HashMap<>();
                // 评论信息
                commentVo.put("comment", comment);
                // 作者信息
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                // 点赞数量
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);
                // 点赞状态
                likeStatus = hostHolder.getUser() == null ? 0 :
                        likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus", likeStatus);

                // 上面查询的是对帖子的评论
                // 现在查询的是对评论的评论
                // 然后把评论的评论也是放到commentVo集合中
                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标，就是说这是你对哪个评论进行的一个评论
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);

                        // 点赞数量
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeCount", likeCount);
                        // 点赞状态
                        likeStatus = hostHolder.getUser() == null ? 0 :
                                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeStatus", likeStatus);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量，显示每一个评论由多少个回复
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);

        return "/site/discuss-detail";
    }

    /**
     * 置顶
     * @param id 帖子的id
     * @return
     */
    //置顶请求
    @RequestMapping(path = "/top/{type}", method = RequestMethod.POST)
    @ResponseBody//异步请求
    public String setTop(int id, @PathVariable("type") int type) {
        if (type == 0) {//如果收到type为0，表示本来是普通帖子，然后现在接收到你的置顶请求，所以我们要把type设置为1然后传到updatType中
            type = 1;
        } else {
            type = 0;
        }
        discussPostService.updateType(id,type);
        //触发发帖事件，同步到ES中
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0);
    }

    /**
     * 加精
     * @param id 帖子的id
     * @return
     */
    //加精请求
    @RequestMapping(path = "/wonderful/{status}", method = RequestMethod.POST)
    @ResponseBody//异步请求
    public String setWonderful(int id,@PathVariable("status") int status) {
        if (status == 0) {
            status = 1;
        } else {
            status = 0;
        }
        discussPostService.updateStatus(id,status);
        //触发发帖事件，同步到ES中
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, id);

        return CommunityUtil.getJSONString(0);
    }


    /**
     * 删除
     * @param id 帖子的id
     * @return
     */
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String setDelete(int id) {
        discussPostService.updateStatus(id, 2);

        // 触发删帖事件
        Event event = new Event()
                .setTopic(TOPIC_DELETE)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0);
    }
}
