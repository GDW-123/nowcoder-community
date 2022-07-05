package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import com.nowcoder.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author GuoDingWei
 * @Date 2022/6/15 11:34
 */

@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;
    /**
     * 用于发送邮件
     */
    @Autowired
    private MailClient mailClient;

    /**
     * 模板引擎
     */
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 域名（本地地址）
     */
    @Value("${community.path.domain}")
    private String domain;

    /**
     * 访问路径
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    //@Autowired
    //private LoginTicketMapper loginTicketMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据用户id来查询用户
     * @param id 用户id
     * @return 返回值是用户对象
     */
    public User findUserById(int id){
        //return userMapper.selectById(id);
        User user = getCache(id);
        if (user == null) {
            user = initCache(id);
        }
        return user;
    }

    /**
     * 用户注册功能
     * @param user 注册的内容中要包括账号，密码，邮箱等，因此直接从前端传入一个User对象最好
     * @return 返回值其实很多类型都是可以的，不过为了保证当注册失败的时候输出多种不同的信息
     *          （比如用户已存在，邮箱已存在，注册失败等一些错误），最好返回一个Map
     */
    public Map<String, Object> register(User user){
        Map<String,Object> map = new HashMap<>();

        //非空判断
        //当下列任意一项出现空值的时候，我们就可以直接结束这个方法了
        if(user == null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空");
            return map;
        }

        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }

        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }

        //验证判断
        //账号验证
        User u = userMapper.selectByName(user.getUsername());
        if(u != null){
            map.put("usernameMsg","该账号已存在!");
            return map;
        }
        //邮箱验证
        u = userMapper.selectByEmail(user.getEmail());
        if(u != null){
            map.put("emailMsg","该邮箱已被注册!");
            return map;
        }

        //程序运行到这里，就说明我们填入的信息是没有任何问题的，这个时候就可以直接注册了
        //使用盐对密码进行加密
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));//取前五位
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        //注册的时候我们需要注意的是我们在前端传入的数据只有账号，密码和邮箱，而数据库的其他字段是需要我们自己设置的
        user.setType(0);//注册的用户默认都是普通用户
        user.setStatus(0);//此时用户账号还没有激活
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/community/activation/101/code
        //我们是没有自己设置id的，但是我们在数据的插入的时候使用的是主键自增，因此这个时候就会自动生成id了
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }

    /**
     * 激活注册码
     * 之所以要传这两个参数，是因为下面这条语句传递的这两个参数
     * String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
     * @param userId 用户id
     * @param code 激活码
     * @return 返回值就是CommunityConstant接口中的常量，表示是否已经激活
     */
    public int activation(int userId, String code) {
        //通过用户注册的时候的主键自增的id来查询当前注册的用户
        User user = userMapper.selectById(userId);
        //如果这个用户的状态为1，表示已经被激活了，就不用重复激活了
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
            //如果这个用户的激活码和我们传入的激活码是一样的话，就表示现在可以激活，
            //就将数据库中的状态改为1，表示激活
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            //否则就表示激活失败
            return ACTIVATION_FAILURE;
        }
    }

    /**
     * 用户登录的业务逻辑层
     * @param username 用户名
     * @param password 密码
     * @param expiredSeconds 登录时间
     * @return
     */
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在!");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活!");
            return map;
        }

        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        //程序运行到这里，表示登录成功了，
        //但是我们在前端传入的数据是不够的，因此我们还需要手动设置一些信息
        //这些信息都是一样的
        //下面就是需要将登陆凭证提交给服务器
        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        //当登录成功的时候就需要设置登录凭证，并且将该凭证放到cookie中（以后会重构来放到redis中）
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        //loginTicketMapper.insertLoginTicket(loginTicket);

        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    /**
     * 退出登录的业务逻辑
     * @param ticket 改变凭证所对应的状态，
     *               0表示有效，1表示无效
     */
    public void logout(String ticket) {
        //loginTicketMapper.updateStatus(ticket, 1);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    /**
     * 根据凭证来查询登录凭证
     * @param ticket 凭证
     * @return 登陆凭证
     */
    public LoginTicket findLoginTicket(String ticket) {
        //return loginTicketMapper.selectByTicket(ticket);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 修改用户头像上传的路径
     * 因为我们需要更新用户的头像，所以我们要传一个用户的id，
     * 来判断是哪一个用户，同时还需要更新用户的头像，所以我们需要来传递头像的路径
     * 这里我们需要注意的是这次是我们将头像上传到本地，以后在项目进行重构的时候会将头像
     * 上传到云服务器上面的，而且本地路径是d：。。。，而我们在访问路径是localhost：。。。
     * @param userId 用户的id
     * @param headerUrl 头像更新的路径
     * @return 返回更新成功的条数
     */
    public int updateHeader(int userId,String headerUrl){
        //return userMapper.updateHeader(userId,headerUrl);
        int rows = userMapper.updateHeader(userId, headerUrl);
        clearCache(userId);
        return rows;
    }

    /**
     * 修改密码
     * @param userId 用户的id
     * @param password 新修改的密码
     * @return 返回执行成功的条数
     */
    public int updatePassword(int userId,String password){
        return userMapper.updatePassword(userId,password);
    }

    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return 用户对象
     */
    public User findUserByName(String username){
        return userMapper.selectByName(username);
    }

    // 1.优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    // 2.取不到时初始化缓存数据
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    // 3.数据变更时清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

    /**
     * 根据用户查询该用户的权限，
     * 便于后序将用户的权限放到Security中
     * @param userId 用户的id
     * @return
     */
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);

        //将判断权限的结果存入到一个集合中
        List<GrantedAuthority> list = new ArrayList<>();
        list.add((GrantedAuthority) () -> {
            switch (user.getType()) {
                case 1:
                    //1表示管理员
                    return AUTHORITY_ADMIN;
                case 2:
                    //2表示版主
                    return AUTHORITY_MODERATOR;
                default:
                    //默认表示普通用户
                    return AUTHORITY_USER;
            }
        });
        return list;
    }
}
