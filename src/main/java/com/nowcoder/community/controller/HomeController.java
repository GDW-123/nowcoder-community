package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author GuoDingWei
 * @Date 2022/6/15 11:37
 */

@Controller
public class HomeController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    /**
     * 跳转到首页的请求
     * @param model model
     * @param page 分页
     * @param orderMode 排序方式，默认为0，表示最新排序
     * @return
     */
    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page,
                               @RequestParam(value = "orderMode",defaultValue = "0") int orderMode){

        //方法调用前，SpringMVC会自动实例化Model和Page，并将Page注入Model
        //因此在thymeleaf中可以直接访问Page对象中的数据
        //我们在传入数据的时候，只需要传入这两组数据即可，其他的都可以来进行计算得到
        //计算数据的总行数，因为是首页，所以不需要通过userId来进行查询
        page.setRows(discussPostService.findDiscussPostRows(0));
        //写出当前的访问路径
        page.setPath("/index?orderMode=" + orderMode);

        //查询帖子,查询到的字段有id,user_id,title,content,type,status,create_time,comment_count,score
        //但是我们要的不是user_id，我们要的是用户名，因此我们还需要遍历这个帖子，然后根据这个用户id来查询用户名
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit(),orderMode);
        //这个集合里面放的是需要前端页面来进行显示的帖子对象和用户对象的map集合
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if(list != null){
            for(DiscussPost post : list){
                //这里放的就是帖子对象和用户对象
                Map<String,Object> map = new HashMap<>();
                //获取用户对象
                User user = userService.findUserById(post.getUserId());
                //将每一篇帖子对象和用户对象都放到map中
                map.put("post",post);
                map.put("user",user);
                //查询该帖子的点赞的数量
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);
                //把每一篇帖子都放到这个list集合中
                discussPosts.add(map);
            }
        }
        //然后传递给前端
        model.addAttribute("discussPosts",discussPosts);
        model.addAttribute("orderMode",orderMode);
        //返回的是模板的路径，也就是templates下的html文件，这里这个目录下的文件以/开头，后面照着写就行
        return "/index";
    }
}
