package cn.alexpy.zuul.filter;

import cn.alexpy.common.aspect.response.ResponseFormat;
import cn.alexpy.zuul.config.Constants;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ERROR_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_ERROR_FILTER_ORDER;

@Slf4j
public class ErrorFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_ERROR_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {

        log.info("==> 错误过滤");

        RequestContext ctx = RequestContext.getCurrentContext();
        error(ctx, Constants.ERR_SYSTEM_UPDATE);
        return null;
    }

    private void error(RequestContext ctx, String msg) {
        // 对该请求禁止路由，也就是禁止访问下游服务
        ctx.setSendZuulResponse(false);
        ctx.getResponse().setStatus(HttpStatus.OK.value());
        ctx.getResponse().setHeader("filter", "false");
        // 处理返回中文乱码
        ctx.getResponse().setContentType("text/html;charset=UTF-8");
        // 设定responseBody供PostFilter使用 502
        ctx.setResponseBody(ResponseFormat.fail(HttpStatus.BAD_GATEWAY.value(), msg));

        try (PrintWriter writer = ctx.getResponse().getWriter()) {
            writer.print(ResponseFormat.fail(HttpStatus.BAD_GATEWAY.value(), msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
