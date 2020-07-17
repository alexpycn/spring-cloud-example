package cn.alexpy.common.aspect;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class BasicLog {

    private static final Logger logger = LoggerFactory.getLogger(BasicLog.class);

    /**
     * debug
     *
     * @param msg
     */
    protected void debugLogger(String msg) {
        logger.debug("==> " + msg);
    }

    protected void debugLogger(String title, String msg, Object arg) {
        logger.warn("==>【" + title + "】" + msg, arg);
    }

    protected void debugLogger(String title, String msg, Object... arg) {
        logger.info("==>【" + title + "】" + msg, arg);
    }

    protected void locationLogger(String title, Integer no) {
        logger.debug("==>【" + title + "】 " + no);
    }

    /**
     * 日志流水信息
     *
     * @param msg
     */
    protected void infoLoggerParam(String param) {
        logger.info("==> 参数 {} 不能为空", param);
    }

    protected void infoLogger(Object msg) {
        logger.info("==> " + msg);
    }

    protected void infoLogger(String msg) {
        logger.info("==> " + msg);
    }

    protected void infoLogger(String title, String msg) {
        logger.warn("==>【" + title + "】" + msg);
    }

    protected void infoLogger(String title, String msg, Object arg) {
        logger.warn("==>【" + title + "】" + msg, arg);
    }

    protected void infoLogger(String title, String msg, Object arg1, Object arg2) {
        logger.info("==>【" + title + "】" + msg, arg1, arg2);
    }

    protected void infoLogger(String title, String msg, Object arg1, Object arg2, Object arg3) {
        logger.info("==>【" + title + "】" + msg, arg1, arg2, arg3);
    }

    protected void reqLogger(String title, String msg, Object... arg) {
        logger.info("==>【" + title + "】" + msg, arg);
    }

    protected void respLogger(String title, String msg, Object... arg) {
        logger.info("==>【" + title + "】" + msg, arg);
    }

    /**
     * 普通错误信息
     * 需要收集的生产环境异常
     * 用于后期完善项目逻辑和BUG修正
     *
     * @param msg
     */
    public void warnLogger(String msg) {
        logger.warn("==> " + msg);
    }

    protected void warnLogger(String title, String msg, Object arg) {
        logger.warn("==>【" + title + "】" + msg, arg);
    }

    protected void warnLogger(String title, String msg, Object... arg) {
        logger.warn("==>【" + title + "】" + msg, arg);
    }

    /**
     * 严重错误
     * 需要人工补偿处理的情况
     * 用于后期生产环境检测可能出现的不确定风险
     *
     * @param msg
     */
    public void errorLogger(String msg) {
        logger.error("==> " + msg);
    }

    protected void errorLogger(String title, String msg, Object arg) {
        logger.error("==>【" + title + "】" + msg, arg);
    }

    protected void errorLogger(String title, String msg, Object... arg) {
        logger.error("==>【" + title + "】" + msg, arg);
    }

    protected void xmlLogger(String msg) {
        logger.info("==> \n" + formatXML(msg));
    }

    protected void jsonLogger(String msg) {
        logger.info("==> " + toPrettyFormat(msg));
    }

    public String formatXML(String inputXMLString) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new StringReader(inputXMLString));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        String requestXML = null;
        XMLWriter writer = null;
        if (document != null) {
            try {
                StringWriter stringWriter = new StringWriter();
                OutputFormat format = OutputFormat.createPrettyPrint();
                writer = new XMLWriter(stringWriter, format);
                try {
                    writer.write(document);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                requestXML = stringWriter.getBuffer().toString();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
        return requestXML;
    }

    /**
     * 格式化输出JSON字符串
     *
     * @return 格式化后的JSON字符串
     */
    protected String toPrettyFormat(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

}
