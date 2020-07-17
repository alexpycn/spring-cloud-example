package cn.alexpy.common.aspect.response;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * @author alexpy
 */
public class ResponseFormat {

    public static String success() {
        return success("", "操作成功");
    }

    public static String successMsg(String msg) {
        return success("", msg);
    }

    public static String success(Object data) {
        ResponseBean json = ResponseBean.of(200, "OK", "操作成功", data);
        return json.toString();
    }

    public static String success(Object data, String msg) {
        ResponseBean json = ResponseBean.of(200, "OK", msg, data);
        return json.toString();
    }

    public static String success(Page<?> page, Object data) {
//        PageResponseBean json = PageResponseBean.of(200, "OK", "操作成功", data, page.getPageNum(), page.getPageSize(), page.getTotal(), page.getPages());
//        PageResponseBean json = PageResponseBean.of(200, "OK", "操作成功", data, page.getPageNum(), page.getPageSize(), page.getTotal(), page.getPages());
        PageResponseBean json = PageResponseBean.builder()
                .code(200)
                .status("OK")
                .message("操作成功")
                .data(data)
                .page(page.getPageNum())
                .rows(page.getPageSize())
                .total(page.getTotal())
                .pages(page.getPages())
                .build();
        return json.toString();
    }

    public static String success(PageInfo<?> page) {
        PageResponseBean json = PageResponseBean.builder()
                .code(200)
                .status("OK")
                .message("操作成功")
                .data(page.getList())
                .page(page.getPageNum())
                .rows(page.getPageSize())
                .total(page.getTotal())
                .pages(page.getPages())
                .build();
        return json.toString();
    }

    public static String success(PageInfo<?> page, Object data) {
        PageResponseBean json = PageResponseBean.builder()
                .code(200)
                .status("OK")
                .message("操作成功")
                .data(data)
                .page(page.getPageNum())
                .rows(page.getPageSize())
                .total(page.getTotal())
                .pages(page.getPages())
                .build();
        return json.toString();
    }

    public static String fail(int code, String msg) {
        ResponseBean json = ResponseBean.of(code, "FAIL", msg, "");
        return json.toString();
    }

    public static JSONObject failJ(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("status", "FAIL");
        json.put("message", msg);
        json.put("data", "");
        return json;
    }

    public static JSONObject successByJSONObject(Object data, PageInfo<?> mPage) {
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("status", "OK");
        json.put("message", "SUCCESS");
        json.put("data", data);
        json.put("pageNum", mPage.getPageNum());
        json.put("PageSize", mPage.getPageSize());
        json.put("total", mPage.getTotal());
        json.put("pages", mPage.getPages());
        return json;
    }

    public static JSONObject successByJSONObject(Object data) {
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("status", "OK");
        json.put("message", "SUCCESS");
        json.put("data", data);
        return json;
    }

    public static JSONObject successByJSONObject() {
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("status", "OK");
        json.put("message", "SUCCESS");
        json.put("data", "");
        return json;
    }

}