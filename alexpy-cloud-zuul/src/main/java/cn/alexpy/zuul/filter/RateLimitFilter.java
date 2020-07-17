package cn.alexpy.zuul.filter;

import cn.alexpy.common.aspect.response.ResponseFormat;
import cn.alexpy.zuul.config.Constants;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/**
 * 限流
 * 所有的资源请求在路由之前进行前置过滤
 * <p>
 * 由于网关所能承受的请求数量有限-防止网关宕机-需要做限流措施
 * 当并发量达到上限-过滤掉其余的请求-保证网关正常运行
 * </p>
 * 2018-10-30 17:55
 *
 * @author alexpy
 */
@Slf4j
public class RateLimitFilter extends ZuulFilter {

    /**
     * 限流参数
     * 每秒生成令牌个数（并发量）
     */
    private static final double RATE_LIMIT_NUM = 500;

    /**
     * 令牌桶-限流
     */
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(RATE_LIMIT_NUM);

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
     * @return 顺序 数字越大表示优先级越低，越后执行
     */
    @Override
    public int filterOrder() {
        return SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    /**
     * 过滤器是否会被执行
     *
     * @return true
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤逻辑
     *
     * @return 过滤结果
     */
    @Override
    public Object run() {

        log.info("==> 限流过滤");

        // 从RequestContext获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.getResponse().setHeader("filter", "true");

        // 限流
        if (!RATE_LIMITER.tryAcquire()) {
            // 令牌用完-不再路由
            log.warn("==> 网络繁忙,请重试");
            // 对该请求禁止路由，也就是禁止访问下游服务
            ctx.setSendZuulResponse(false);
            ctx.getResponse().setStatus(HttpStatus.OK.value());
            ctx.getResponse().setHeader("filter", "false");
            // 处理返回中文乱码
            ctx.getResponse().setContentType("text/html;charset=UTF-8");
            // 设定responseBody供PostFilter使用
            ctx.setResponseBody(ResponseFormat.fail(HttpStatus.TOO_MANY_REQUESTS.value(), Constants.ERR_SYSTEM_UPDATE));
        }
        return null;
    }

}
