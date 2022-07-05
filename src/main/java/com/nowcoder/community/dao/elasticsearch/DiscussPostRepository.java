package com.nowcoder.community.dao.elasticsearch;

import com.nowcoder.community.entity.DiscussPost;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author GuoDingWei
 * @Date 2022/6/22 12:59
 */


@Repository
//注意，这里不是mapper，mapper是mybatis的专有注解
//Repository是spring提供的dao层的注解
//这里不需要写任何的方法，我们只需要声明实体类的类型和主键的类型即可，es已经帮我们实现了这些方法的
//我们得到的数据也还是从mysql中取出来的数据，然后存入到es中
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}
