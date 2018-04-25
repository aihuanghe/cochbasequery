package com.asiainfo.busi;

import com.asiainfo.db.Connection;
import com.asiainfo.db.Connmanage;
import com.asiainfo.until.MD5RowKeyGenerator;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcctItemHyhBusi {
    private final static Log log = LogFactory.getLog(AcctItemHyhBusi.class.getSimpleName());

    private List<String> listcol = new ArrayList<String>();

    public AcctItemHyhBusi() {
        listcol.add(new String("phoneNo"));
        listcol.add(new String("star"));
        listcol.add(new String("sumBillCallNum"));
        listcol.add(new String("sumBillCallDur"));
        listcol.add(new String("sumGprsBillFlow"));
        listcol.add(new String("maxCallMonth"));
        listcol.add(new String("maxGprsMonth"));
        listcol.add(new String("maxCallDur"));
        listcol.add(new String("maxBillFlow"));
        listcol.add(new String("callnumrn"));
        listcol.add(new String("calldurarn"));
        listcol.add(new String("gprsrn"));
        listcol.add(new String("billrn"));
        listcol.add(new String("billFee"));
        listcol.add(new String("rentFee"));
        listcol.add(new String("rentPercent"));
        listcol.add(new String("voiceCallFee"));
        listcol.add(new String("voicePercent"));
        listcol.add(new String("smmsFee"));
        listcol.add(new String("smmsPercent"));
        listcol.add(new String("wirelessFee"));
        listcol.add(new String("wirelessPercent"));
        listcol.add(new String("otherFee"));
        listcol.add(new String("otherPercent"));
        listcol.add(new String("app1"));
        listcol.add(new String("app2"));
        listcol.add(new String("app3"));
        listcol.add(new String("userCnt"));
        listcol.add(new String("userOtherAreaCnt"));
        listcol.add(new String("maxBalanceMonth"));
        listcol.add(new String("hyhCreatRn"));
        listcol.add(new String("hyhSignCnt"));
        listcol.add(new String("hyhSignDate"));
        listcol.add(new String("hyhMaxRemain"));
        listcol.add(new String("hyhChangeFlowCnt"));
        listcol.add(new String("hyhFlowFeeCnt"));
        listcol.add(new String("hyhLoginMaxTime"));
        listcol.add(new String("hyhLoginMaxCnt"));
        listcol.add(new String("hyhPintuCnt"));
        listcol.add(new String("hyhPintuWinCnt"));
        listcol.add(new String("hyhPayCnt"));
        listcol.add(new String("hyhYcCnt"));


    }

    public JSONObject getQueryRes(JSONObject jsparam) {
        try {
            log.info("===============请求数据:" + jsparam + "");
            String startKey = new MD5RowKeyGenerator().generatePrefix(jsparam.getString("phoneNo")) + jsparam.getString("phoneNo");
            String endKey = new MD5RowKeyGenerator().generatePrefix(jsparam.getString("phoneNo")) + String.valueOf(Long.valueOf(jsparam.getString("phoneNo")) + 1);
            String tablename = "DM_FUN_ACCTITEM_HYH_HBASE_YM";

            List<String> res = this.scanQuery(startKey, endKey, tablename);

            JSONObject js = transOut(res);
            js.put("rtcode", 0);
            js.put("rtmsg", "查询正常");
            return JSONObject.fromObject(js);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject js = new JSONObject();
        js.put("rtcode", -1);
        js.put("rtmsg", "查询错误");
        return js;
    }


    private List<String> scanQuery(String startRowKey, String endRowKey, String tablename) throws Exception {
        ResultScanner rs = Connmanage.scanQuery(startRowKey, endRowKey, tablename);
        List<String> re = new ArrayList<String>();

        Integer i = 0;
        for (Result res : rs) {
            re.add(Bytes.toString(res.getValue(Bytes.toBytes(new String("F")), Bytes.toBytes(new String("COL")))));
        }
        return re;
    }

    public JSONObject transOut(List<String> data) throws Exception {
        JSONObject js = new JSONObject();
        try {
            List<Map<String, String>> result = new ArrayList<Map<String, String>>();
            for (String tmp : data) {
                log.debug("==============data================");
                log.debug(tmp);
                if (tmp.length() <= 0)
                    continue;
                log.debug("idx=" + tmp.indexOf("|"));
                log.debug("==================================");
                if (tmp.indexOf("|") < 0)
                    continue;
                Map<String, String> mapr = new HashMap<String, String>();
                String[] strlist = tmp.split(new String("\\|"));
                log.debug("=======strlist=[" + strlist.length + "]   col size=[" + listcol.size() + "]================");
                for (int i = 0; i < listcol.size(); i++) {
                    mapr.put(listcol.get(i), strlist[i]);
                }
                result.add(mapr);

            }
            JSONArray arr = JSONArray.fromObject(result);

            js.put("datas", arr);
            return js;
        } catch (Exception e) {
            throw e;
        }

    }
}
