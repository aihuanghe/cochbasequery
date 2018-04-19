package com.asiainfo.db;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import com.ailk.oci.ocnosql.common.exception.ClientConnectionException;
import com.ailk.oci.ocnosql.common.exception.ClientRuntimeException;
import com.ailk.oci.ocnosql.common.util.DistributedCacheUtil;
public class Connection {

    private final static Log log = LogFactory.getLog(Connection.class.getSimpleName());

    private static Properties confProperty;

    private Configuration conf;

    private ThreadPoolExecutor threadPool;

    private static volatile Connection INSTANCE = null;

    protected org.apache.hadoop.hbase.client.Connection conn;

   // private static String runStatus="MAIN";
    /**
     * 可选的参数
     */

    private String msg;

    public static Connection getInstance() {
        if(INSTANCE == null) {
            synchronized (Connection.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Connection();
                }
            }
        }
        return INSTANCE;
    }

    private Connection() {
        readConf(); //获取客户端配置文件

        conf = HBaseConfiguration.create();

        log.info("*************#@@@@@"+conf.get(CommonConstants.USERNAME_CLIENT_KEYTAB_FILE));
        log.info("*************#@@@@@"+conf.get("phoenix.queryserver.keytab.file"));

        connect(); //用客户端的host/port配置覆盖hbase的配置
      //  init(); //创建执行线程池，初始化系统参数
       // printValues();
    }

    /**
     * 获取客户端配置文件--------------------1
     */
    private static void readConf() {
        log.info("[ocnosql]start read conf file " + CommonConstants.FILE_NAME + ".");
        FileInputStream fis = null;
        confProperty = new Properties();
        try {


            URL url = Connection.class.getClassLoader().getResource(CommonConstants.FILE_NAME);
            log.info("[ocnosql]start read conf file " + CommonConstants.FILE_NAME + "[" + url + "].");
            InputStream in = Connection.class.getClassLoader()
                    .getResourceAsStream(CommonConstants.FILE_NAME);
            if (in == null) {
                throw new ClientRuntimeException("plz check if file "
                        + CommonConstants.FILE_NAME + " in classpath!");
            }
            confProperty.load(in);
            log.info("***************#### " + in);
            log.info("***************###### " + confProperty.toString());
            log.info("*************#"+confProperty.getProperty("java.security.auth.login.config"));
            log.info("*************#"+confProperty.getProperty("java.security.krb5.conf"));
            log.info("*************#"+confProperty.getProperty("username.client.keytab.file"));
            log.info("*************#"+confProperty.getProperty("username.client.kerberos.principal"));

           // log.info("*************#"+confProperty.stringPropertyNames());

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            log.info("[ocnosql]end read conf file " + CommonConstants.FILE_NAME + ".");
        }
    }

    /**
     * 连接参数设置---------------2
     *
     * @throws ClientConnectionException
     */
    private void connect() throws ClientConnectionException {
        log.info("start connect ocnosql");

        String hosts;// zookeeper主机名称，多个主机名称以,号分隔开

        String port;// 客户端端口号

        hosts = get(CommonConstants.HOSTS, null); //读取zookeeper主机名称

        port = get(CommonConstants.PORT, null); //读取zookeeper端口号


        if (StringUtils.isEmpty(hosts) || StringUtils.isEmpty(port)) {
            msg = "zookeeper hosts or port must not be null";
            log.error(msg);
            throw new ClientConnectionException(msg);
        }

        ////////////////////////////////////////////////////////
        conf = HBaseConfiguration.create();
        //用客户端的配置覆盖hbase的配置
        conf.set(CommonConstants.HOSTS, hosts);
        conf.set(CommonConstants.PORT, port);
        //配置hadoop url
        conf.set(CommonConstants.HDFS_URL, get(CommonConstants.HDFS_URL, null));
        conf.addResource("hbase-resource/hbase-site.xml");
        String kerbros=get(CommonConstants.KERBROS,null);
        if(kerbros!=null &&"true".equals(kerbros))
        login();


        try {
            conn = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            log.error("[ocnosq] cann't get connection to hbase.", e);
            throw new ClientConnectionException("[ocnosql] get connection to hbase.", e);
        }
        log.info("connected hbase successfully");
    }


    /**
     * login kerberos
     */
    public void login() {
        try {
            log.info("*************############"+conf);
            log.info("*************#"+conf.get(CommonConstants.JAVA_SECURITY_AUTH_LOGIN_CONFIG));
            log.info("*************0000#"+conf.get("java.security.auth.login.config"));
            log.info("*************#"+confProperty.getProperty("java.security.auth.login.config"));
            log.info("*************#"+confProperty.getProperty("java.security.krb5.conf"));
            log.info("*************#"+confProperty.getProperty("username.client.keytab.file"));
            log.info("*************#"+confProperty.getProperty("username.client.kerberos.principal"));

//             Login.login(conf,
//                    conf.get(CommonConstants.JAVA_SECURITY_AUTH_LOGIN_CONFIG),
//                    conf.get(CommonConstants.JAVA_SECURITY_KRB5_CONF),
//                    conf.get(CommonConstants.USERNAME_CLIENT_KEYTAB_FILE),
//                    conf.get(CommonConstants.USERNAME_CLIENT_KERBEROS_PRINCIPAL)
//            );

            
            Login.login(conf,
                    confProperty.getProperty(CommonConstants.JAVA_SECURITY_KRB5_REALM),
                    confProperty.getProperty(CommonConstants.JAVA_SECURITY_KRB5_KDC),
                    confProperty.getProperty(CommonConstants.USERNAME_CLIENT_KEYTAB_FILE),
                    confProperty.getProperty(CommonConstants.USERNAME_CLIENT_KERBEROS_PRINCIPAL),
                    confProperty.getProperty(CommonConstants.HADOOP_HOME_DIR)
            );
            

        } catch (IOException e) {
            throw new ClientConnectionException("login kerberos failed", e);
        }
    }


    /**
     * 创建执行线程池，初始化系统参数-----------3
     */
    protected void init() {
        conf = HBaseConfiguration.create();
        int corePoolSize = Integer.parseInt(get(
                CommonConstants.THREADPOOL_COREPOOLSIZE, "50"));// 默认并发运行的线程数
        int maximumPoolSize = Integer.parseInt(get(
                CommonConstants.THREADPOOL_MAXPOOLSIZE, "80"));// 默认池中最大线程数
        long keepAliveTime = Integer.parseInt(get(
                CommonConstants.THREADPOOL_KEEPALIVETIME, "60"));// 默认池中线程最大等待时间

        log.warn("[ocnosql]create default threadPool,corePoolSize="
                + corePoolSize + " maximumPoolSize=" + maximumPoolSize
                + " keepAliveTime=" + keepAliveTime + "s.");

        //创建执行线程池
        threadPool = new ThreadPoolExecutor(
                corePoolSize, // 并发运行的线程数
                maximumPoolSize, // 池中最大线程数
                keepAliveTime, // 池中线程最大等待时间
                TimeUnit.SECONDS, // 池中线程最大等待时间单位
                new LinkedBlockingQueue<Runnable>(), // 池中队列维持对象
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy() // 超过最大线程数方法，此处为自动重新调用execute方法
        );

        // 重置数据压缩方式
        conf.set(CommonConstants.COMPRESSOR, get(CommonConstants.COMPRESSOR,
                CommonConstants.DEFAULT_COMPRESSOR));



        // 重置rowkey生成方式
        conf.set(CommonConstants.ROWKEY_GENERATOR, get(
                CommonConstants.ROWKEY_GENERATOR,
                ""));

        //重置导入文件分隔符
        conf.set(CommonConstants.SEPARATOR, get(
                CommonConstants.SEPARATOR,
                CommonConstants.DEFAULT_SEPARATOR)); //默认为"/t"

        //重置rpc timeout，默认为三分钟
        conf.setLong(CommonConstants.HBASE_RPC_TIMEOUT,
                Long.parseLong(get("hbase.rpc.timeout", "180000")));

        log.debug("[ocnosql]create default threadPool success.");

        conf.set(CommonConstants.DISTRIBUTED_CACHE_FILE_PATH, get(CommonConstants.DISTRIBUTED_CACHE_FILE_PATH, ""));
        conf.set(CommonConstants.DISTRIBUTED_CACHE_ARCHIVE_PATH, get(CommonConstants.DISTRIBUTED_CACHE_ARCHIVE_PATH, ""));
        try {
            DistributedCacheUtil.distributedCache(conf, FileSystem.get(conf));
        } catch (IOException e) {
            throw new ClientRuntimeException("add files to distribute cache exception.", e);
        }

    }
    /**
     * -----------------------------4
     */

    public void printValues() {
        log.info("*************************************************************************************");
        log.info(CommonConstants.HDFS_URL + "[" + conf.get(CommonConstants.HDFS_URL) + "]");
        log.info(CommonConstants.HOSTS + "[" + conf.get(CommonConstants.HOSTS) + "]");
        log.info(CommonConstants.PORT + "[" + conf.get(CommonConstants.PORT) + "]");
        log.info("hbase.client.retries.number[" + conf.get("hbase.client.retries.number") + "]");
        log.info(CommonConstants.HBASE_RPC_TIMEOUT + "[" + conf.get(CommonConstants.HBASE_RPC_TIMEOUT) + "]");
        log.info(CommonConstants.SEPARATOR + "[" + conf.get(CommonConstants.SEPARATOR) + "]");
        log.info(CommonConstants.COMPRESSOR + "[" + conf.get(CommonConstants.COMPRESSOR) + "]");
        log.info(CommonConstants.ROWKEY_GENERATOR + "[" + get(CommonConstants.ROWKEY_GENERATOR, "") + "]");
        log.info(CommonConstants.DISTRIBUTED_CACHE_FILE_PATH + "[" + get(CommonConstants.DISTRIBUTED_CACHE_FILE_PATH, "") + "]");
        log.info(CommonConstants.DISTRIBUTED_CACHE_ARCHIVE_PATH + "[" + get(CommonConstants.DISTRIBUTED_CACHE_ARCHIVE_PATH, "") + "]");
        log.info("*************************************************************************************");
    }







    public static String get(String key, String defaultValue) {
        if (confProperty == null) {
            readConf();
        }
        String value = confProperty.getProperty(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    public Map<String, String> retrieveValueByPrefix(String prefix) {
        if (confProperty == null) {
            readConf();
        }
        Map<String, String> propertyValue = new HashMap<String, String>();
        Set<String> prpertyNames = confProperty.stringPropertyNames();
        for (String key : prpertyNames) {
            if (key.startsWith(prefix)) {
                propertyValue.put(key, confProperty.getProperty(key));
            }
        }
        return propertyValue;
    }



    public Configuration getConf() {
        return conf;
    }

    public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    public org.apache.hadoop.hbase.client.Connection getHBaseConnection() {
        if(conn == null || conn.isClosed()) {
            synchronized (this) {
                if(conn == null || conn.isClosed()) {
                    connect();
                }
            }
        }
        return conn;
    }
    public void close()
    {
        synchronized (this){
            try {
                conn.close();
            } catch (IOException e) {
                conn=null;
            }

        }
    }

}

