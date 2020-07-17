package cn.alexpy.zuul.filter;

import cn.alexpy.common.aspect.response.ResponseFormat;
import cn.alexpy.zuul.config.Constants;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

import java.util.concurrent.TimeUnit;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/**
 * 权限校验
 * <p>
 * 来自网络的请求是不安全的-所以要做权限校验
 * 有一些接口是需要登录后才能访问的
 * 由于是分布式部署
 * 所以统一在网关层做权限校验
 * 不符合条件的请求不再路由-直接返回错误码
 * </p>
 *
 * @author alexpy
 */
@Slf4j
public class AccessFilter extends ZuulFilter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 过滤器的类型 pre表示请求在路由之前被过滤
     *
     * @return 类型
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * 过滤器的执行顺序
     *
     * @return 顺序 数字越大 表示优先级越低 越后执行
     */
    @Override
    public int filterOrder() {
        return SERVLET_DETECTION_FILTER_ORDER;
    }

    /**
     * 过滤器是否会被执行
     *
     * @return true
     */
    @Override
    public boolean shouldFilter() {
        // 从RequestContext获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取自定义Header头
        String filter = ctx.getResponse().getHeader("filter");
        return filter == null || filter.equals("true");
    }

    /**
     * 过滤逻辑
     *
     * @return 过滤结果
     */
    @Override
    public Object run() {

        log.info("==> 权限过滤");


        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String token = request.getHeader("token");
        String sign = request.getHeader("sign");
        String uri = request.getRequestURI();
        if (StringUtils.isBlank(uri)) {
            error(ctx, Constants.ERR_ILLEGAL_MSG);
        } else {
            String[] split = uri.split("/");
            if (split.length < 4) {
                error(ctx, Constants.ERR_ILLEGAL_MSG);
            }
            switch (split[3]) {
                case "public":
                    break;
                case "token":
                    if (StringUtils.isBlank(token)) {
                        loginOverdue(ctx, Constants.ERR_EXPIRE_MSG);
                    }
                    // true 限制 false 不限制
                    if (limit(token)) {
                        error(ctx, Constants.ERR_SYSTEM_OFTEN);
                    }
                    break;
                case "sign":
                    if (StringUtils.isBlank(token)) {
                        loginOverdue(ctx, Constants.ERR_EXPIRE_MSG);
                    }
                    if (StringUtils.isBlank(sign)) {
                        error(ctx, Constants.ERR_ILLEGAL_MSG);
                    }
                    // true 限制 false 不限制
                    if (limit(token)) {
                        error(ctx, Constants.ERR_SYSTEM_OFTEN);
                    }
                    break;
                default:
                    error(ctx, Constants.ERR_ILLEGAL_MSG);
            }

            ctx.getResponse().setHeader("x-real-ip", getIPAddress(request));
            log.info("==> TOKEN: " + token);
            log.info("==> URI: " + uri + "\n");
        }
        return null;
    }

    private boolean limit(String token) {

        if (redisTemplate == null || StringUtils.isBlank(token)) {
            return false;
        }

        // 是否限制访问
        String disable = redisTemplate.opsForValue().get("request-disable:" + token);

        // 访问次数记录
        boolean exist = Boolean.parseBoolean(redisTemplate.hasKey("request-limit:" + token) + "");
        if (exist) {
            // 访问次数加1
            redisTemplate.opsForValue().increment("request-limit:" + token);
        } else {
            // 初始化访问次数
            redisTemplate.opsForValue().set("request-limit:" + token, "1", 10, TimeUnit.SECONDS);
        }

        // 获取访问次数
        String count = redisTemplate.opsForValue().get("request-limit:" + token);
        if (StringUtils.isNotBlank(count) && StringUtils.isBlank(disable)) {
            int num = Integer.parseInt(count);
            if (num >= 5) {
                // 是否存在于黑名单
                int coeff = 1;
                String coefficient = redisTemplate.opsForValue().get("request-black:" + token);
                if (StringUtils.isNotBlank(coefficient) && Integer.parseInt(coefficient) > 0) {
                    // 系数加1
                    redisTemplate.opsForValue().increment("request-black:" + token);
                    coeff = Integer.parseInt(coefficient);
                } else {
                    redisTemplate.opsForValue().set("request-black:" + token, "1", Constants.MONTH, TimeUnit.SECONDS);
                }
                if (num < 10) {
                    redisTemplate.opsForValue().set("request-disable:" + token, count, Constants.MINUTE * coeff, TimeUnit.SECONDS);
                } else if (num < 15) {
                    redisTemplate.opsForValue().set("request-disable:" + token, count, Constants.MINUTE * 5 * coeff, TimeUnit.SECONDS);
                } else {
                    redisTemplate.opsForValue().set("request-disable:" + token, count, Constants.HOUR * coeff, TimeUnit.SECONDS);
                }
            }
        }

        return StringUtils.isNotBlank(disable);
    }

    /**
     * 非法访问
     *
     * @param ctx
     * @param msg
     */
    private void error(RequestContext ctx, String msg) {
        // 对该请求禁止路由，也就是禁止访问下游服务
        ctx.setSendZuulResponse(false);
        ctx.getResponse().setStatus(HttpStatus.OK.value());
        ctx.getResponse().setHeader("filter", "false");
        // 处理返回中文乱码
        ctx.getResponse().setContentType("text/html;charset=UTF-8");
        // 设定responseBody供PostFilter使用 429
        ctx.setResponseBody(ResponseFormat.fail(HttpStatus.TOO_MANY_REQUESTS.value(), msg));
    }

    /**
     * 登录过期
     *
     * @param ctx
     * @param msg
     */
    private void loginOverdue(RequestContext ctx, String msg) {
        // 对该请求禁止路由，也就是禁止访问下游服务
        ctx.setSendZuulResponse(false);
        ctx.getResponse().setStatus(HttpStatus.OK.value());
        ctx.getResponse().setHeader("filter", "false");
        // 处理返回中文乱码
        ctx.getResponse().setContentType("text/html;charset=UTF-8");
        // 设定responseBody供PostFilter使用 401
        ctx.setResponseBody(ResponseFormat.fail(HttpStatus.UNAUTHORIZED.value(), msg));
    }

    public String getIPAddress(HttpServletRequest request) {

        String ip = null;

        // X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            // Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            // WL-Proxy-Client-IP：webLogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            // HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            // X-Real-IP：nginx 服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        // 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        // 还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }

        log.info("==> IP: " + ip);
        return ip;
    }

}
