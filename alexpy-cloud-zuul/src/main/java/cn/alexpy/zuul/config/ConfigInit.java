package cn.alexpy.zuul.config;

import cn.alexpy.zuul.filter.AccessFilter;
import cn.alexpy.zuul.filter.ErrorFilter;
import cn.alexpy.zuul.filter.RateLimitFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author alexpy
 * 初始化
 */
@Configuration
public class ConfigInit {

    /**
     * 资源过滤器
     *
     * @return 资源过滤器
     */
    @Bean
    public RateLimitFilter rateLimitFilter() {
        return new RateLimitFilter();
    }

    /**
     * 权限过滤器
     *
     * @return 权限过滤器
     */
    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }

    /**
     * 错误过滤器
     *
     * @return 错误过滤器
     */
    @Bean
    public ErrorFilter errorFilter() {
        return new ErrorFilter();
    }
}
