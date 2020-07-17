package cn.alexpy.common.utils;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * 签名规则
 */
public class SignRule {

    /**
     * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     *
     * @return boolean
     */
    public static boolean verifySign(String characterEncoding, SortedMap<String, String> packageParams, String mchKey) {
        StringBuilder sb = new StringBuilder();
        Set es = packageParams.entrySet();
        for (Object e : es) {
            Map.Entry entry = (Map.Entry) e;
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k).append("=").append(v).append("&");
            }
        }

        sb.append("key=").append(mchKey);

        //算出摘要
        String mySign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toLowerCase();
        String tenPaySign = (packageParams.get("sign")).toLowerCase();

        return tenPaySign.equals(mySign);
    }


    /**
     * @param characterEncoding 编码格式
     * @param parameters        请求参数
     * @return
     * @Description：sign签名
     */
    public static String createSign(String characterEncoding, SortedMap<String, String> parameters, String mchKey) {
        StringBuilder sb = new StringBuilder();
        Set es = parameters.entrySet();
        for (Object e : es) {
            Map.Entry entry = (Map.Entry) e;
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k).append("=").append(v).append("&");
            }
        }
        sb.append("key=").append(mchKey);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

}
