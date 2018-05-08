package com.mine.oa;

import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Configuration
public class SecurityCorsConfiguration {

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader(CorsConfiguration.ALL);
        config.addAllowedMethod(CorsConfiguration.ALL);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    // @Bean
    // public TaskExecutor threadPool() {
    // ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    // taskExecutor.setQueueCapacity(200);
    // taskExecutor.setCorePoolSize(5);
    // taskExecutor.setMaxPoolSize(8);
    // taskExecutor.initialize();
    // return taskExecutor;
    // }

    @Bean
    public ForkJoinPool forkJoinPool() {
        return new ForkJoinPool();
    }

    /***
     * 定时任务默认使用的Single线程池，每次只会有一个线程执行
     * 
     * @see ScheduledTaskRegistrar#scheduleTasks()
     * @return 共定时任务使用的线程池
     */
    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService taskScheduler() {
        return Executors.newScheduledThreadPool(5);
    }

}
