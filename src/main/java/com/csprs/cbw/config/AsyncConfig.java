package com.csprs.cbw.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * <p>Title: ThreadPoolExecutorConfig.java</p>
 * <p>Description: 可以不用寫，但這是用來指定Async線程池的設定</p>
 * <p>Company: CSPRS</p>
 *
 * @author sung{Ping
 * @date 2022年3月11日
 * @version 1.0
 */
@Configuration
@EnableAsync
public class AsyncConfig {
	
	@Bean(name = "executor")
	public Executor executor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(3);
		executor.setMaxPoolSize(3);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("async-");
		executor.initialize();
		return executor;
	}
	
	
}
