//package com.ktrental.util;
//
//import java.security.spec.AlgorithmParameterSpec;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//
//public class CryptoUtil {
//    private static String secretKey = null;
//    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
//
//    public CryptoUtil() {
//    }
//
//    public static void reset() {
//        secretKey = null;
//    }
//
//    public static void makeSecretKey4KTRENTAL() {
//        String key = "_KTRENTAL!";
//        secretKey = sdf.format(new Date()) + key;
//    }
//
//    public static String encrypt(String src) {
//        String encrypted = "";
//        try {
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//            AlgorithmParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
//            SecretKeySpec newKey = new SecretKeySpec(secretKey.getBytes(), "AES");
//            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
//            encrypted = new String(cipher.doFinal(src.getBytes()));
//        } catch (Exception e) {
//            encrypted = e.toString();
//            System.out.println(e);
////            return null;
//        }
////        try {
////            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
////            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
////            cipher.init(1, skeySpec);
////            encrypted = byteArrayToHex(cipher.doFinal(src.getBytes("UTF-8")));
////        } catch (Exception var4) {
////            System.out.println(var4);
////        }
//
//        return encrypted;
//    }
//
//    public static String decrypt(String src) {
//        String decrypted = "";
//        try {
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//            AlgorithmParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
//            SecretKeySpec newKey = new SecretKeySpec(secretKey.getBytes(), "AES");
//            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
//            decrypted = new String(cipher.doFinal(src.getBytes()));
//        } catch (Exception e) {
//            decrypted = "[ERROR] INVALID KEY.";
//            decrypted = e.toString();
////            return null;
//        }
////        String decrypted = "";
////
////        try {
////            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
////            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
////            cipher.init(2, skeySpec);
////            decrypted = new String(cipher.doFinal(hexToByteArray(src)));
////        } catch (Exception var4) {
////            decrypted = "[ERROR] INVALID KEY.";
////        }
//
//        return decrypted;
//    }
//
//    public static byte[] hexToByteArray(String hex) {
//        if (hex != null && hex.length() != 0) {
//            byte[] ba = new byte[hex.length() / 2];
//
//            for(int i = 0; i < ba.length; ++i) {
//                ba[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
//            }
//
//            return ba;
//        } else {
//            return null;
//        }
//    }
//
//    public static String byteArrayToHex(byte[] ba) {
//        if (ba != null && ba.length != 0) {
//            StringBuffer sb = new StringBuffer(ba.length * 2);
//
//            for(int x = 0; x < ba.length; ++x) {
//                String hexNumber = "0" + Integer.toHexString(255 & ba[x]);
//                sb.append(hexNumber.substring(hexNumber.length() - 2));
//            }
//
//            return sb.toString();
//        } else {
//            return null;
//        }
//    }
//}
