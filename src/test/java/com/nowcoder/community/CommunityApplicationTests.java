package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.dao.AlphaDaoMyBatisImpl;
import com.nowcoder.community.service.AlphaService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
//表示以该配置类来启动主程序
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests  implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Test
	public void testApplicationContext(){
		System.out.println(applicationContext);
	}

	//bean既可以通过名称，也可以通过类型来创建
	@Test
	void contextLoads() {
		System.out.println(applicationContext);
		//通过类型来获取java对象，这里使用接口或者实现类都是可以的，相当于注解@Autowired
		AlphaDao bean = applicationContext.getBean(AlphaDao.class);
		//然后通过对象来调用实例方法
		System.out.println("AlphaDao-->"+bean.select());
		//通过名称来进行注入,相当于@Qualifier("alphaImpl")
		AlphaDao alphaImpl = applicationContext.getBean("alphaImpl", AlphaDaoMyBatisImpl.class);
		System.out.println("AlphaDaoMyBatisImpl-->"+alphaImpl.select());
	}

	//默认创建的bean是单例的
	@Test
	void testBeanManager(){
		AlphaService bean = applicationContext.getBean(AlphaService.class);
		System.out.println(bean);
		bean = applicationContext.getBean(AlphaService.class);
		System.out.println(bean);
	}

	//获取配置类的bean
	@Test
	void testBeanConfig(){
		SimpleDateFormat bean = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(bean.format(new Date()));
	}

	//依赖注入
	@Autowired
	private AlphaDao alphaDao;

	//测试类：
	@Test
	void testDI(){
		System.out.println(alphaDao.select());
	}
}
