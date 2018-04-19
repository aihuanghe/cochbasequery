package com.asiainfo.db;

import com.ailk.oci.ocnosql.common.exception.ClientConnectionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;



public class Connmanage {
    private final static Log log = LogFactory.getLog(Connmanage.class.getSimpleName());
    private  static org.apache.hadoop.hbase.client.Connection conn=null;
    private static String usingType="MAIN";
    private static  void getConn()
    {

        try {
            conn = Connection.getInstance().getHBaseConnection();
            usingType="MAIN";
            if(conn==null || conn.isClosed())
            {
                conn = ConnectionHA.getInstance().getHBaseConnection();
                usingType="HA";
            }

        }
        catch (ClientConnectionException e)
        {
            e.printStackTrace();
            throw e;
        }

    }


    public static ResultScanner scanQuery(String StratRowkey, String EndRowKey, String tablename) throws Exception {
        try {
            log.debug("================scan query=================");
            log.debug("StratRowkey=[" + StratRowkey + "]");
            log.debug("EndRowKey=[" + EndRowKey + "]");
            //org.apache.hadoop.hbase.client.Connection conn = Connection.getInstance().getHBaseConnection();
            getConn();
            Table query = conn.getTable(TableName.valueOf(tablename));
            Scan scan = new Scan();
            scan.setStartRow(Bytes.toBytes(StratRowkey));
            scan.setStopRow(Bytes.toBytes(EndRowKey));
            // Filter filter=new RowFilter(CompareFilter.CompareOp.EQUAL,new SubstringComparator("sdsfsf"));
            ResultScanner rs = query.getScanner(scan);
            return rs;
        }
        catch (Exception e)
        {

            e.printStackTrace();
            if("MAIN".equals(usingType))
                Connection.getInstance().close();
            else
            {
                ConnectionHA.getInstance().close();
            }
            throw e;
            //throw e;
        }

    }

    public static boolean tableExists(String tableName) throws Exception{
        HBaseAdmin hBaseAdmin = null;
        boolean existsFlag = false;
        try {
            getConn();
            hBaseAdmin =  (HBaseAdmin)conn.getAdmin();
            existsFlag = hBaseAdmin.tableExists(tableName);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }finally {
            if(hBaseAdmin!=null){
                hBaseAdmin.close();
            }
        }

        return existsFlag;
    }

    public static HTableDescriptor[] getTableList () throws Exception
    {
        try {
            getConn();
            Admin admin = conn.getAdmin();
            HTableDescriptor[] allTable = admin.listTables();
            return allTable;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if("MAIN".equals(usingType))
                Connection.getInstance().close();
            else
            {
                ConnectionHA.getInstance().close();
            }
            throw e;
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            throw new Exception("sdfsf");
        }
    }
    public void checkConn()
    {
//        synchronized(Connmanage.class) {
//            if (connMain.status == -1) {
//                connMain.close();
//                connMain.getHBaseConnection();
//                usingType="MAIN";
//            }
//
//            if (connHa.status == -1) {
//                connHa.close();
//                connHa.getHBaseConnection();
//                if(!"MAIN".equals(usingType))
//                {
//                    usingType="HA";
//                }
//            }
//        }
    }

}
