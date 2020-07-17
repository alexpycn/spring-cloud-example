package cn.alexpy.zuul.config;

/**
 * @author alexpy
 */
public class Constants {

    /**
     * 非法访问
     */
    public static final String ERR_ILLEGAL_MSG = "你访问个辣子";

    /**
     * 登录过期
     */
    public static final String ERR_EXPIRE_MSG = "未登录";

    /**
     * 系统升级中/网络繁忙,请重试
     */
    public static final String ERR_SYSTEM_UPDATE = "网络繁忙,请重试";

    /**
     * 访问过于频繁,请稍后再试
     */
    public static final String ERR_SYSTEM_OFTEN = "访问过于频繁,请稍后再试";

    public static final Integer MINUTE = 60;

    public static final Integer HOUR = 60 * 60;

    public static final Integer DAY = 60 * 60 * 24;

    public static final Integer MONTH = 60 * 60 * 24 * 30;


}
