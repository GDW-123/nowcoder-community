package com.nowcoder.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author GuoDingWei
 * @Date 2022/6/27 10:12
 */
@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
