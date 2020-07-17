package cn.alexpy.common.enums;

public enum  Env {

    // 开发环境
    ENV_DEV("dev"),

    // 测试环境
    ENV_TEST("test"),

    // 生产环境
    ENV_PROD("prod");

    private final String env;

    Env(String env) {
        this.env = env;
    }

//    public static void main(String[] args) {
//        System.out.println(Env.ENV_DEV);
//        System.out.println(Env.ENV_TEST);
//        System.out.println(Env.ENV_PROD);
//    }
}
