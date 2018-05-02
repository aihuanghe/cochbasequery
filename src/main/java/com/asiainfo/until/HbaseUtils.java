package com.asiainfo.until;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class HbaseUtils {

    private static final Properties prop = new Properties();

    private static final Logger LOGGER = LoggerFactory.getLogger(HbaseUtils.class);

    private static String[] ADDRESS;

    static {
        try {
            prop.load(HbaseUtils.class.getClassLoader().getResourceAsStream("hbase.properties"));
            ADDRESS = prop.getProperty("address").split(",");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取同步设置时间的服务地址
     *
     * @return 地址集合
     */
    private static List<String> getSetList() {
        List<String> list = new ArrayList<String>();
        for (String add : ADDRESS) {
            list.add(add + "/asyncSetDate");
        }
        LOGGER.info("需要同步设置时间服务地址：" + list);
        return list;
    }

    /**
     * 获取设置自动服务地址
     *
     * @return 服务地址集合
     */
    private static List<String> getAutoswitchList() {
        List<String> list = new ArrayList<String>();
        for (String add : ADDRESS) {
            list.add(add + "/asyncAutoSwitch");
        }
        LOGGER.info("需要同步设置自动服务地址：" + list);
        return list;
    }

    /**
     * 同步设置日期
     *
     * @param date
     */
    public static void asyncSetDate(String date) {
        List<String> list = getSetList();
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", date);
        String result;
        for (String url : list) {
            result = HttpClientUtil.doGet(url, params);
            LOGGER.info("同步手动设置时间： 请求地址=" + url + ",参数=" + params + ",结果=" + result);
        }
    }

    /**
     * 同步自动设置
     */
    public static void asyncAutoSwitch() {
        List<String> list = getAutoswitchList();
        String result;
        for (String url : list) {
            result = HttpClientUtil.doGet(url, null);
            LOGGER.info("同步自动设置时间： 请求地址=" + url + ",结果=" + result);
        }
    }

    public static void main(String[] args) {
        getSetList();
        getAutoswitchList();
    }


}
