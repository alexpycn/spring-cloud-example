package cn.alexpy.common.aspect;

import cn.alexpy.common.aspect.interceptor.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

public class Assert {

    /**
     * 值不能为null
     *
     * @param obj
     * @param msg 异常消息
     */
    public static void checkNull(@Nullable Object obj, String msg) throws ServiceException {
        if (obj == null) {
            throw ServiceException.paramException(msg);
        }
    }

    /**
     * 值不能为null
     *
     * @param obj
     * @param msg 异常消息
     */
    public static void checkNull(@Nullable Object obj) throws ServiceException {
        if (obj == null) {
            throw ServiceException.paramException();
        }
    }

    /**
     * 值不能为null
     *
     * @param obj
     * @param msg 异常消息
     */
    public static void checkNull(@Nullable String obj, String msg) throws ServiceException {
        if (StringUtils.isBlank(obj)) {
            throw ServiceException.paramException(msg);
        }
    }

    /**
     * 值不能为null
     *
     * @param obj
     */
    public static void checkNull(@Nullable String obj) throws ServiceException {
        if (StringUtils.isBlank(obj)) {
            throw ServiceException.paramException();
        }
    }

    /**
     * 值不能为false
     *
     * @param obj
     * @param msg 异常消息
     */
    public static void isTrue(@Nullable Boolean flag, @Nullable String msg) throws ServiceException {
        if (!flag) {
            throw ServiceException.paramException(msg);
        }
    }

    /**
     * 验证最小值
     *
     * @param obj
     * @param min 最小值
     */
    public static void checkMin(@Nullable Integer obj) throws ServiceException {
        if (obj == null || obj < 0) {
            throw ServiceException.paramException();
        }
    }

    /**
     * 验证最小值
     *
     * @param obj
     * @param min 最小值
     * @param msg 异常消息
     */
    public static void checkMin(@Nullable Integer obj, String msg) throws ServiceException {
        if (obj == null || obj < 0) {
            throw ServiceException.paramException(msg);
        }
    }

    /**
     * 验证最小值
     *
     * @param obj
     * @param min 最小值
     * @param msg 异常消息
     */
    public static void checkMin(@Nullable Integer obj, @Nullable Integer min, String msg) throws ServiceException {
        if (obj == null || obj < min) {
            throw ServiceException.paramException(msg);
        }
    }

    /**
     * 验证最小值
     *
     * @param obj
     * @param min 最小值
     * @param msg 异常消息
     */
    public static void checkMin(@Nullable BigDecimal obj, @Nullable Double min, String msg) throws ServiceException {
        if (obj == null || obj.compareTo(BigDecimal.ZERO) < 0 || obj.doubleValue() < min) {
            throw ServiceException.paramException(msg);
        }
    }

    /**
     * 验证最小值
     *
     * @param obj
     * @param min 最小值
     * @param msg 异常消息
     */
    public static void checkMin(@Nullable BigDecimal obj, @Nullable BigDecimal min, String msg) throws ServiceException {
        if (obj == null || obj.compareTo(min) < 0) {
            throw ServiceException.paramException(msg);
        }
    }

    /**
     * 验证最小值
     *
     * @param obj
     * @param min 最小值
     */
    public static void checkMin(@Nullable Integer obj, @Nullable Integer min) throws ServiceException {
        if (obj == null || obj < min) {
            throw ServiceException.paramException();
        }
    }

    /**
     * 验证最大值
     *
     * @param obj
     * @param max 最大值
     * @param msg 异常消息
     */
    public static void checkMax(@Nullable Integer obj, @Nullable Integer max, String msg) throws ServiceException {
        if (obj == null || obj > max) {
            throw ServiceException.paramException(msg);
        }
    }

    /**
     * 验证最大值
     *
     * @param obj
     * @param max 最大值
     */
    public static void checkMax(@Nullable Integer obj, @Nullable Integer max) throws ServiceException {
        if (obj == null || obj > max) {
            throw ServiceException.paramException();
        }
    }

}
