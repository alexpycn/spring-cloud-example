package cn.alexpy.zuul;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * API网关服务
 *
 * @author Alex
 * @ EnableZuulProxy 启用网关路由
 */
@Slf4j
@EnableZuulProxy
@EnableDiscoveryClient
@EnableHystrix
@EnableHystrixDashboard
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ZuulApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ZuulApplication.class, args);
        int length = context.getBeanDefinitionNames().length;

        log.info("Zuul 启动初始化了 {} 个 Bean", length);
    }

    // prometheus monitor
    @Bean
    MeterRegistryCustomizer<MeterRegistry> configure() {
        return (registry) -> registry.config().commonTags("job", "zuul");
    }
}
