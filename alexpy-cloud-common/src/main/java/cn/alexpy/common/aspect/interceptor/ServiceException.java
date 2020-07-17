package cn.alexpy.common.aspect.interceptor;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.servlet.ServletException;

/**
 * @author alexpy
 * 0    00   000
 * 级别 模块  编号
 * <p>
 * 1 系统 2 服务 3 业务
 * 00 通用
 * <p>
 * 通用
 * 500 系统错误
 * 403 参数错误
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends ServletException {

    // 服务器异常
    public static ServiceException systemException() {
        return new ServiceException(500, "网络异常");
    }

    public static ServiceException serverException(String msg) {
        return new ServiceException(500, msg);
    }

    // 未登录
    public static ServiceException unLoginException() {
        return new ServiceException(401, "登录过期");
    }

    // 未登录
    public static ServiceException unLoginException(String msg) {
        return new ServiceException(401, msg);
    }

    // 参数错误
    public static ServiceException paramException() {
        return new ServiceException(403, "参数错误");
    }

    // 参数错误
    public static ServiceException paramException(String msg) {
        return new ServiceException(403, msg);
    }

    // 其他异常
    public static ServiceException clientException(String msg) {
        return new ServiceException(406, msg);
    }

    // 非法请求
    public static ServiceException illegalException() {
        return new ServiceException(429, "非法请求");
    }

    protected ServiceException() {
    }

    protected ServiceException(int code, String message) {

        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

}
