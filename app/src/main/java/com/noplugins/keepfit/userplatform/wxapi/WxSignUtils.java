package com.noplugins.keepfit.userplatform.wxapi;

import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class WxSignUtils {

    /**
     * 调起微信APP支付，签名
     * 生成签名
     */
    public static String genPackageSign(LinkedHashMap<String, String> params, String key) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(key);
        String packageSign = getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return packageSign;
    }

    public static String genPackageSign(String sign) {

        String packageSign = getMessageDigest(sign.toString().getBytes()).toUpperCase();
        return packageSign;
    }
    /**
     * md5加密
     *
     * @param buffer
     * @return
     */
    public static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取随机数
     *
     * @return
     */
    public static String genNonceStr() {
        Random random = new Random();
        return getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }


    /**
     * 获取时间戳
     *
     * @return
     */
    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

}
