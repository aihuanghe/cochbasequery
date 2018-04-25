package com.asiainfo.until;

import java.security.MessageDigest;

public class MD5RowKeyGenerator {
    private MessageDigest md = null;

    public String generate(String oriRowKey, String needHashValue, String[] currenRowdata, int[] posIndex, String appendValue) {
        return null;
    }

    public Object generate(String oriRowKey) {
        return generatePrefix(oriRowKey) + oriRowKey;
    }

    public synchronized String getMD5(String oriRowKey) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = oriRowKey.getBytes();

            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            mdInst.update(btInput);

            byte[] md = mdInst.digest();

            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            return new String(str);
        } catch (Exception localException) {
        }
        return null;
    }

    public Object generatePrefix(String oriRowKey) {
        String result = getMD5(oriRowKey);
        return result.substring(1, 2) + result.substring(3, 4) + result.substring(5, 6);
    }

    public static void main(String[] args) {
        System.out.println(new MD5RowKeyGenerator().generatePrefix("13477343118"));
    }
}

