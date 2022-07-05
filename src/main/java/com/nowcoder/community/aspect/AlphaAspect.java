package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Author GuoDingWei
 * @Date 2022/6/19 16:26
 */
@Component
@Aspect
public class AlphaAspect {

    //pointcut切点方法不用写任何逻辑是规范,仅仅是为了定义一个切点而已,逻辑都在那个注解上。
    //下面都是使用的这个注解来实现对具体的方法实现切入的
    //方法的返回值+com.nowcoder.community.service（包名）+方法类+方法名（参数）
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut() {

    }

    //在实例方法之前调用的时候执行
    @Before("pointcut()")
    public void before() {
        //System.out.println("before");
    }

    //在实例方法之后调用的时候执行
    @After("pointcut()")
    public void after() {
        //System.out.println("after");
    }

    //在实例方法有返回值之后的时候执行
    @AfterReturning("pointcut()")
    public void afterRetuning() {
        //System.out.println("afterRetuning");
    }

    //在实例方法抛异常的时候执行
    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        //System.out.println("afterThrowing");
    }

    //在实例方法的前后调用的时候都执行
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //System.out.println("around before");
        Object obj = joinPoint.proceed();
        //System.out.println("around after");
        return obj;
    }

}
