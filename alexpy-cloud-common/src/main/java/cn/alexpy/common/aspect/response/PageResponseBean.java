package cn.alexpy.common.aspect.response;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PageResponseBean extends ResponseBean {

    public static final long serialVersionUID = 1L;

    @Builder
    public PageResponseBean(int code, String status, String message, Object data, int page, int rows, long total, int pages) {
        super.code = code;
        super.status = status;
        super.message = message;
        super.data = data;
        this.page = page;
        this.rows = rows;
        this.total = total;
        this.pages = pages;
    }

    // 当前页码
//    @NonNull
    private int page;
    // 当前页条数
//    @NonNull
    private int rows;
    // 总数
//    @NonNull
    private long total;
    // 总页数
//    @NonNull
    private int pages;

}
