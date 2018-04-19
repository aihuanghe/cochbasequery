package com.asiainfo.db;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CacheTableUtils {

    static {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               Set<Map.Entry<TableName,Table>> set = CacheTableUtils.tables.entrySet();
                for (Map.Entry<TableName,Table> entry: set){
                    try {
                        entry.getValue().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                CacheTableUtils.clean();
            }
        };
        Thread clean = new Thread(runnable);
        clean.setDaemon(true);
        clean.start();
    }

    private static Map<TableName, Table> tables = new HashMap<TableName, Table>();

    public static synchronized Table getTable(TableName key) {
        synchronized (tables) {
            return tables.get(key);
        }
    }

    public static boolean exists(TableName key) {
        boolean flag = false;
        synchronized (tables) {
            if (tables.containsKey(key)) {
                if (tables.get(key) != null) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    public static void clean() {
        synchronized (tables) {
            tables.clear();
        }
    }

    public static void setTable(TableName key, Table table) {
        synchronized (tables) {
            if (!exists(key)) {
                tables.put(key, table);
            }
        }

    }

}
