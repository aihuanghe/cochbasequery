/*    */ package com.asiainfo.until;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.security.MessageDigest;
/*    */ 
/*    */ public class MD5RowKeyGenerator
/*    */ {
/*  8 */   private MessageDigest md = null;
/*    */   
/*    */   public String generate(String oriRowKey, String needHashValue, String[] currenRowdata, int[] posIndex, String appendValue)
/*    */   {
/* 11 */     return null;
/*    */   }
/*    */   
/*    */   public Object generate(String oriRowKey)
/*    */   {
/* 15 */     return generatePrefix(oriRowKey) + oriRowKey;
/*    */   }
/*    */   
/*    */   public synchronized String getMD5(String oriRowKey)
/*    */   {
/* 20 */     char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*    */     try
/*    */     {
/* 22 */       byte[] btInput = oriRowKey.getBytes();
/*    */       
/* 24 */       MessageDigest mdInst = MessageDigest.getInstance("MD5");
/*    */       
/* 26 */       mdInst.update(btInput);
/*    */       
/* 28 */       byte[] md = mdInst.digest();
/*    */       
/* 30 */       int j = md.length;
/* 31 */       char[] str = new char[j * 2];
/* 32 */       int k = 0;
/* 33 */       for (int i = 0; i < j; i++)
/*    */       {
/* 34 */         byte byte0 = md[i];
/* 35 */         str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
/* 36 */         str[(k++)] = hexDigits[(byte0 & 0xF)];
/*    */       }
/* 38 */       return new String(str);
/*    */     }
/*    */     catch (Exception localException) {}
/* 41 */     return null;
/*    */   }
/*    */   
/*    */   public Object generatePrefix(String oriRowKey)
/*    */   {
/* 45 */     String result = getMD5(oriRowKey);
/* 46 */     return result.substring(1, 2) + result.substring(3, 4) + result.substring(5, 6);
/*    */   }
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/* 50 */     System.out.println(new MD5RowKeyGenerator().generatePrefix("13477343118"));
/*    */   }
/*    */ }


/* Location:           \\Vboxsvr\其他\
 * Qualified Name:     com.asiainfo.dr.MD5RowKeyGenerator
 * JD-Core Version:    0.7.0.1
 */