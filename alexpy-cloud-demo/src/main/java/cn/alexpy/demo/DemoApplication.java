package cn.alexpy.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@EnableDiscoveryClient
// 禁用数据库自动配置
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication(scanBasePackages = "cn.alexpy.demo")
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        int length = context.getBeanDefinitionNames().length;
        log.info("==>");
        log.info("==> C O M P L E T E");
        log.info("==>");
        log.info("==> Demo启动完成 初始化了 {} 个 Bean", length);
        log.info("==>");

    }
}
