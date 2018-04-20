package com.asiainfo.busi;

import com.asiainfo.db.Connmanage;
import com.asiainfo.until.MD5RowKeyGenerator;
import com.asiainfo.until.SwitchDateUtils;
import com.asiainfo.until.Tools;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.*;

public class CustGroupCheckBusi {
    private final static Log log = LogFactory.getLog(CustGroupCheckBusi.class.getSimpleName());

    private List<String> scanQuery(String StratRowkey, String EndRowKey, String tablename ,org.apache.hadoop.hbase.client.Connection conn) throws Exception {
        log.debug("================scan query=================");
        log.debug("StratRowkey=["+StratRowkey+"]");
        log.debug("EndRowKey=["+EndRowKey+"]");
        // org.apache.hadoop.hbase.client.Connection conn = Connection.getInstance().getHBaseConnection();
//        Table query = conn.getTable(TableName.valueOf(tablename));
//        Scan scan = new Scan();
//        scan.setStartRow(Bytes.toBytes(StratRowkey));
//        scan.setStopRow(Bytes.toBytes(EndRowKey));
//        ResultScanner rs = query.getScanner(scan);
        ResultScanner rs =Connmanage.scanQuery(StratRowkey,EndRowKey,tablename);
        List<String> re=new ArrayList<String>();


        Integer i=0;
        for (Result res : rs) {
            log.debug("res=["+Bytes.toString(res.getRow())+"]");
            re.add(Bytes.toString(res.getRow()));
        }
        //query.close();
        log.debug("=================================");
        return  re;
    }

    public String queryExist(String val) throws Exception {
        JSONObject js=JSONObject.fromObject(val);
        String telno=js.getString("telnum");
        String groupid=js.getString("custgroupid");
        String tableName="COC_CUSTOMER_GROUP_";
        if (SwitchDateUtils.IS_AUTOSWITCH){
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, -1);
            Date yestoday = c.getTime();
            tableName+= Tools.date2Str(yestoday,"yyyyMMdd");
            log.info("自动切换日期：" + tableName);
        }else{
            tableName+=Tools.convertDate(SwitchDateUtils.SWITCH_DATE);
            log.info("手动切换日期：" + tableName);
        }
//        HTableDescriptor[] allTable =Connmanage.getTableList();
//        List<String> tables = new ArrayList<String>();
//        for (HTableDescriptor hTableDescriptor : allTable) {
//            tables.add(hTableDescriptor.getNameAsString());
//            //System.out.println(hTableDescriptor.getNameAsString());
//        }
//        //admin.close();
        log.info("-------------------");
        log.info("tableName="+tableName);
        Map<String,String> rt=new HashMap<String, String>();
        if(Connmanage.tableExists(tableName))
        {
            String rowkey=new MD5RowKeyGenerator().generatePrefix(telno)+telno+"|"+groupid;
            String tmp1=groupid.substring(6, groupid.length());
            Long val1=Long.valueOf(tmp1);
            val1=val1+1l;
            String tmpgrop = String.format("%08d",val1);
            String enrow=new MD5RowKeyGenerator().generatePrefix(telno)+telno+"|"+groupid.substring(0,6)+tmpgrop;
            if(scanQuery(rowkey,enrow,tableName,null).size()>0)
            {
                rt.put("retcode","0");
                rt.put("errmsg","数据存在");
                rt.put("telstatus","0");
            }
            else
            {
                rt.put("retcode","0");
                rt.put("errmsg","数据不存在");
                rt.put("telstatus","-1");
            }
        }
        else
        {
            rt.put("retcode","-2");
            rt.put("errmsg","表"+tableName+"未生成");
        }

        return JSONObject.fromObject(rt).toString();
    }

    public String queryInfo(String val) throws Exception {
        JSONObject js=JSONObject.fromObject(val);
        String telno=js.getString("telnum");
        //String groupid=js.getString("custgroupid");
        String tableName="COC_CUSTOMER_GROUP_";
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date yestoday = c.getTime();
        tableName+= Tools.date2Str(yestoday,"yyyyMMdd");
//        org.apache.hadoop.hbase.client.Connection conn = Connection.getInstance().getHBaseConnection();
//        Admin admin =conn.getAdmin();
//        HTableDescriptor[] allTable = admin.listTables();
//        HTableDescriptor[] allTable =Connmanage.getTableList();
//        List<String> tables = new ArrayList<String>();
//        for (HTableDescriptor hTableDescriptor : allTable) {
//            tables.add(hTableDescriptor.getNameAsString());
//            //System.out.println(hTableDescriptor.getNameAsString());
//        }
//        //admin.close();
        log.info("-------------------");
        log.info("tableName="+tableName);


        Map<String,String> rt=new HashMap<String, String>();
        if(Connmanage.tableExists(tableName))
        {
            String rowkey=new MD5RowKeyGenerator().generatePrefix(telno)+telno;
            String endrwo=new MD5RowKeyGenerator().generatePrefix(telno)+String.valueOf(Long.valueOf(telno)+1);
            log.info("rowkey="+rowkey+" endrwo="+endrwo);
            List<String> strlist= scanQuery(rowkey,endrwo,tableName,null);
            List<Map<String,String>> tmp=new ArrayList<Map<String, String>>();
            for(String str:strlist)
            {
                Map<String,String> tmpmap=new HashMap<String, String>();
                String groupid=str.replaceAll(rowkey+"\\|","");
                groupid=groupid.substring(0,groupid.length()-16);
                tmpmap.put("id",groupid);
                tmp.add(tmpmap);
            }
            rt.put("retcode","0");
            rt.put("errmsg","");
            JSONObject jrt= JSONObject.fromObject(rt);
            jrt.put("custgrouplist",JSONArray.fromObject(tmp));
            return jrt.toString();
        }
        else
        {
            rt.put("retcode","-2");
            rt.put("errmsg","表"+tableName+"未生成");
            return JSONObject.fromObject(rt).toString();
        }


    }

}
