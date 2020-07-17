package cn.alexpy.common.aspect;


import com.github.binarywang.java.emoji.EmojiConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author alexpy
 */
public abstract class BaseAbstractClass extends BasicLog {

    /**
     * Emoji 图标处理
     */
    protected EmojiConverter emojiConverter = EmojiConverter.getInstance();

    /**
     * 系统名称
     */
    @Value("${spring.application.name}")
    protected String systemName;

    protected static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
    protected static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    public static String getBaseDate() {
        return sdf.format(new Date());
    }

    protected Gson gson = new GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();
}
