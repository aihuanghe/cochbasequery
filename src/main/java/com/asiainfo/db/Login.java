package com.asiainfo.db;
import java.io.IOException;
import java.net.InetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.security.User;
import org.apache.hadoop.security.UserGroupInformation;

public class Login {

    public static final Log LOG = LogFactory.getLog(Login.class);

    public static void login(Configuration conf, String realm, String kdc, String keytabPath, String principal,String hadoophome) throws IOException {
        if (User.isHBaseSecurityEnabled(conf)) {

            LOG.info("begin login kerberos ...");
//            LOG.info(CommonConstants.JAVA_SECURITY_AUTH_LOGIN_CONFIG + ": " + jassPath);
//            LOG.info(CommonConstants.JAVA_SECURITY_KRB5_CONF + ": " + krb5Path);
            LOG.info(CommonConstants.USERNAME_CLIENT_KEYTAB_FILE + ": " + keytabPath);
            LOG.info(CommonConstants.USERNAME_CLIENT_KERBEROS_PRINCIPAL + ": " + principal);
            
            // jaas.conf file, it is included in the client pakcage file
            System.setProperty("hadoop.home.dir", hadoophome);
            
            // jaas.conf file, it is included in the client pakcage file
//            System.setProperty(CommonConstants.JAVA_SECURITY_AUTH_LOGIN_CONFIG, jassPath);
//
//            // set the kerberos server info,point to the kerberosclient
            //System.setProperty(CommonConstants.JAVA_SECURITY_KRB5_CONF, krb5Path);

            System.setProperty("java.security.krb5.realm", realm);
            System.setProperty("java.security.krb5.kdc", kdc);
            
            
            
            // set the keytab file name
            conf.set(CommonConstants.USERNAME_CLIENT_KEYTAB_FILE, keytabPath);
            // set the user's principal
            conf.set(CommonConstants.USERNAME_CLIENT_KERBEROS_PRINCIPAL, principal);

//            User.login(conf,
//                    CommonConstants.USERNAME_CLIENT_KEYTAB_FILE,
//                    CommonConstants.USERNAME_CLIENT_KERBEROS_PRINCIPAL,
//                    InetAddress.getLocalHost().getCanonicalHostName());
            UserGroupInformation.setConfiguration(conf);
			UserGroupInformation.loginUserFromKeytab(principal, keytabPath);

            LOG.info("login kerberos success!");
        }
    }
}
