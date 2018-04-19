package com.asiainfo.db;

public class CommonConstants {

    public final static String FILE_NAME = "client-runtime.properties"; //客户端配置文件
    public final static String FILE_NAME_HA ="client-runtime-ha.properties";
    public final static String KERBROS ="hbase.kerbros";
    /** 连接参数 */
    public final static String HOSTS = "hbase.zookeeper.quorum"; //zookeeper服务器
    public final static String PORT = "hbase.zookeeper.property.clientPort";//zookeeper端口

    public final static String COMPRESSOR = "compressor.class";// 压缩和解压类名称
    public final static String DEFAULT_COMPRESSOR = "com.ailk.oci.ocnosql.common.compress.HbaseNullCompress";// 压缩和解压类名称
    public final static String RECORD_SEPARATOR = "`";// 记录间分隔符
    public final static String VALUE_SEPARATOR = "value.separator";// 记录间分隔符
    public final static String PREFIX_TOKEN = "asterisk";// 前缀搜索标记
    public final static String PREFIX_TOKEN2 = "asterisk2";// 前缀搜索标记
    public final static String CLIENT_TYPE = "client.type.class";//查询客户端类型

    public final static String TABLE_NAME = "tableName";// 表名称
    public final static String TABLE_CONF = "table_conf";
    public final static String COLUMNS = "importtsv.columns";// 字段串
    public final static String INPUTFORMAT = "importtsv.inputFormat";//bulkload时指定inputFormat
    public final static String NOTNEEDLOADCOLUMNS = "importtsv.notloadcolumns";// 在列定义中不需要导入的列 ,多个以逗号分隔
    public final static String SEPARATOR = "importtsv.separator"; // 分隔符
    public final static String MONITOR_LOG_PATH = "importtsv.monitorLog.path"; //稽核日志输出路径
    public final static String DEFAULT_SEPARATOR = "\t"; // 分隔符
    public final static String INSERTVERTICAL = "importtsv.bulk.insertvertical";// 字段串
    public final static String ROWKEYCOLUMN = "importtsv.rowkeycolumn"; // 指定生成rowkey的列
    public final static String ROWKEYGENERATOR = "importtsv.rowkeyGenerator";//指定生成rowkey的算法
    public final static String ALGOCOLUMN = "importtsv.algocolumn";//指定对哪个列执行算法。
    public final static String ROWKEYCALLBACK = "importtsv.callback"; //指定对算法之后的rowkey加工的处理类名(全名)
    public final static String DEFAULT_ROWKEYCALLBACK = "com.ailk.oci.ocnosql.client.rowkeygenerator.GenRKCallBackDefaultImpl";//key后缀回调函数
    public final static String INPUT = "input"; // 输入路径
    public final static String EXPOTR_OUTPUT = "export_output"; // 输出路径
    public final static String IMPORT_TMP_OUTPUT = "importtsv.bulk.output"; // 临时输出路径
    public final static String SKIPBADLINE = "importtsv.skip.bad.lines"; // 是否滤掉过错误行
    public final static String BULKLOAD_ERROR_OUTPUT = "importtsv.bulk.erroroutput";
    public final static String STORAGE_STRATEGY = "storage_strategy"; //存储策略：singleimporttsv/mutipleimporttsv
    public final static String BATCH_PUT = "batch_put";	//put批量提交
    public final static String HBASE_MAXPUTNUM = "hbase.maxPutNum";	//put批量提交最大数量
    public final static String ROWKEY_EXPRESSION = "rowkey.expression";

    public final static String HDFS_URL = "fs.defaultFS";

    public final static String ROW_KEY = "HBASE_ROW_KEY"; // rowkey关键字
    public final static String ROWKEY_COLUMN_INDEX = "HBASE_ROW_KEY"; // rowkey关键字的索引位置
    public final static String SINGLE_FAMILY = "family"; // 列族名称
    public final static String DEFAULT_SINGLE_FAMILY = "f"; // 默认列族名称
    public final static String ROWKEY_GENERATOR = "rowkey.generator"; // ROWKEY产生类
    public final static String ROWKEY_UNIQUE = "rowkey.unique";	//rowkey末尾唯一标示

    public static String HBASE_RPC_TIMEOUT= "hbase.rpc.timeout";
    public static String QUERY_CACHE_SIZE = "query.cache.size"; // regionserver每次缓存的条数
    public static String QUERY_BATCH_SIZE = "query.batch.size"; // regionserver每次返回的条数
    public static String QUERY_RESULT_MAX_SIZE="query.result.max.size";// 客户端每次SCAN查询最多获取的记录条数。超过部分将自动截断。
    public static Long DEFAULT_QUERY_RESULT_MAX_SIZE=50000L;

    public final static String DB_DRIVERCLASSNAME = "db.driverClassName";
    public final static String DB_URL = "db.url";
    public final static String DB_USERNAME = "db.username";
    public final static String DB_PASSWORD = "db.password";

    /**线程池相关参数*/
    public final static String THREADPOOL_COREPOOLSIZE = "threadpool.core.size";// 默认并发运行的线程数
    public final static String THREADPOOL_MAXPOOLSIZE = "threadpool.max.size";// 默认池中最大线程数
    public final static String THREADPOOL_KEEPALIVETIME = "threadpool.keepalivetime";// 默认池中线程最大等待时间

    /* ocnosqlTab.xml中rowkey生成规则的配置 */
    public final static String GENROWKEYSTEP = "genrowkeystep";
    public final static String COLUMNINDEX = "columnIndex";
    public final static String ALGORITHM = "algorithm";
    public final static String CALLBACK = "callback";

    public final static String COMPRESS_FAIL_FLAG = "compress_fail";

    public final static String DECOMPRESS_FAIl_FLAG = "decompress_fail";

    public static final String DISTRIBUTED_CACHE_FILE_PATH = "distributed.cache.file.path";
    public static final String DISTRIBUTED_CACHE_ARCHIVE_PATH = "distributed.cache.archive.path";


    /*查询多副本配置常量*/
    public final static String SCAN_CONSISTENCY ="scan.consistency";
    public final static String SCAN_RESULT_STALE ="scan.result.stale";
    public final static String GET_CONSISTENCY ="get.consistency";
    public final static String GET_RESULT_STALE ="get.result.stale";

    /**
     * for kerberos login
     */
    public final static String JAVA_SECURITY_AUTH_LOGIN_CONFIG = "java.security.auth.login.config";
    public final static String JAVA_SECURITY_KRB5_CONF = "java.security.krb5.conf";
    public final static String USERNAME_CLIENT_KEYTAB_FILE = "username.client.keytab.file";
    public final static String USERNAME_CLIENT_KERBEROS_PRINCIPAL = "username.client.kerberos.principal";
    public final static String JAVA_SECURITY_KRB5_REALM = "java.security.krb5.realm";
    public final static String JAVA_SECURITY_KRB5_KDC = "java.security.krb5.kdc";
    public final static String HADOOP_HOME_DIR = "hadoop.home.dir";
        
}
