package com.asiainfo.busi;

import com.asiainfo.db.Connmanage;
import com.asiainfo.until.MD5RowKeyGenerator;
import com.asiainfo.until.SwitchDateUtils;
import com.asiainfo.until.Tools;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.*;

public class CustGroupCheckBusi {
    private final static Log log = LogFactory.getLog(CustGroupCheckBusi.class.getSimpleName());

    private List<String> scanQuery(String startRowKey, String endRowKey, String tableName, org.apache.hadoop.hbase.client.Connection conn) throws Exception {
        log.debug("================scan query begin=================");
        log.debug("tableName=[" + tableName + "]");
        log.debug("startRowKey=[" + startRowKey + "]");
        log.debug("endRowKey=[" + endRowKey + "]");
        ResultScanner rs = Connmanage.scanQuery(startRowKey, endRowKey, tableName);
        List<String> re = new ArrayList<String>();

        for (Result res : rs) {
            log.debug("res=[" + Bytes.toString(res.getRow()) + "]");
            re.add(Bytes.toString(res.getRow()));
        }
        log.debug("================scan query end=================");
        return re;
    }

    public String queryExist(String val) throws Exception {
        log.info("--------------查询号码是否在某个客户群组中 queryExist begin--------------");
        log.info("queryExist params:" + val);
        JSONObject js = JSONObject.fromObject(val);
        String telno = js.getString("telnum");
        String groupid = js.getString("custgroupid");
        String tableName = "COC_CUSTOMER_GROUP_";
        if (SwitchDateUtils.IS_AUTOSWITCH) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            //昨天的日期
            c.add(Calendar.DAY_OF_MONTH, -1);
            tableName += Tools.date2Str(c.getTime(), "yyyyMMdd");
            log.info("自动切换表名：" + tableName);
        } else {
            if (StringUtils.isNotBlank(SwitchDateUtils.SWITCH_DATE)) {
                tableName += Tools.convertDate(SwitchDateUtils.SWITCH_DATE);
                log.info("手动切换日期：" + SwitchDateUtils.SWITCH_DATE);
                log.info("手动切换表名：" + tableName);
            } else {
                throw new RuntimeException("设置的手动切换日期为null");
            }
        }

        log.info("-------------------");
        log.info("tableName=" + tableName);
        Map<String, String> rt = new HashMap<String, String>();
        if (Connmanage.tableExists(tableName)) {
            String rowKey = new MD5RowKeyGenerator().generatePrefix(telno) + telno + "|" + groupid;
            log.info("查询客户群startRow:" + rowKey);
//            //<jsonParam>{"telnum":"18802789452","custgroupid":"KHQ27000010649"}</jsonParam>
//            //oc513477343118|KHQ27000010649
            if (scanQuery(rowKey, rowKey, tableName, null).size() > 0) {
                rt.put("retcode", "0");
                rt.put("errmsg", "数据存在");
                rt.put("telstatus", "0");
            } else {
                rt.put("retcode", "0");
                rt.put("errmsg", "数据不存在");
                rt.put("telstatus", "-1");
            }
        } else {
            rt.put("retcode", "-2");
            rt.put("errmsg", "表" + tableName + "未生成");
        }

        String  resultInfo= JSONObject.fromObject(rt).toString();
        log.info("queryExist result:"+ resultInfo);
        log.info("--------------查询号码是否在某个客户群组中 queryExist begin--------------");
        return resultInfo;
    }

    public String queryInfo(String val) throws Exception {
        log.info("---------查询客户群组信息 queryInfo begin----------");
        log.info("queryInfo params:" + val);
        String resultInfo;
        JSONObject js = JSONObject.fromObject(val);
        String telno = js.getString("telnum");
        //String groupid=js.getString("custgroupid");
        String tableName = "COC_CUSTOMER_GROUP_";
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        //昨天日期
        c.add(Calendar.DAY_OF_MONTH, -1);
        tableName += Tools.date2Str(c.getTime(), "yyyyMMdd");
        log.info("tableName=" + tableName);


        Map<String, String> rt = new HashMap<String, String>();
        if (Connmanage.tableExists(tableName)) {
            //13477343118 - 开始行： oc513477343118
            String rowKey = new MD5RowKeyGenerator().generatePrefix(telno) + telno;
            log.info("rowKey=" + rowKey + ",endKey=" + rowKey);
            List<String> strlist = scanQuery(rowKey, rowKey, tableName, null);
            List<Map<String, String>> tmp = new ArrayList<Map<String, String>>();
            for (String str : strlist) {
                Map<String, String> tmpmap = new HashMap<String, String>();
                String groupid = str.replaceAll(rowKey + "\\|", "");
                groupid = groupid.substring(0, groupid.length() - 16);
                tmpmap.put("id", groupid);
                tmp.add(tmpmap);
            }
            rt.put("retcode", "0");
            rt.put("errmsg", "");
            JSONObject jrt = JSONObject.fromObject(rt);
            jrt.put("custgrouplist", JSONArray.fromObject(tmp));
            resultInfo = jrt.toString();
        } else {
            rt.put("retcode", "-2");
            rt.put("errmsg", "表" + tableName + "未生成");
            resultInfo = JSONObject.fromObject(rt).toString();
        }

        log.info("queryInfo result:"+ resultInfo);
        log.info("---------查询客户群组信息 queryInfo end----------");
        return resultInfo;


    }

}
