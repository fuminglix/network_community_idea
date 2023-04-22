package com.haue.utils;


import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Data
public class CheckUtils {
    public static final String SIGN = "sign";
    public static final String KEY = "key";
    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String getMD5Sign(Map<String, Object> params, String key, List<String> excludeKeyList) {
        if (null == params) {
            return StringUtils.EMPTY;
        }
        String checkStr = getCheckStr(params, excludeKeyList);
        checkStr = checkStr + "&" + KEY + "=" + key;
        //  System.out.println(checkStr);
        String md5EncodeStr = md5Encode(checkStr);
        // log.debug("参数:{},签名串：{} 签名后的字符串为：{}", params.toString(), checkStr, md5EncodeStr);
        return md5EncodeStr;
    }

    private static String getCheckStr(Map<String, Object> params, List<String> excludeKeyList) {
        TreeSet<String> keySet = new TreeSet<>(params.keySet());
        Iterator<String> keyIterator = keySet.iterator();
        String keyTmp = "";
        StringBuilder checkStrBuilder = new StringBuilder();
        while (keyIterator.hasNext()) {
            keyTmp = keyIterator.next();
            Object valTmp = params.get(keyTmp);
            String value = "";
            if (valTmp == null) {
                continue;
            }
            if (excludeKeyList != null && excludeKeyList.contains(keyTmp)) {
                continue;
            }
            if (valTmp instanceof Map) {
                Map<String, Object> tmpObj = (Map) valTmp;
                value = getCheckStr(tmpObj, excludeKeyList);
            } else if (valTmp instanceof List) {
                StringBuilder sub = new StringBuilder();
                for (int i = 0; i < ((List) valTmp).size(); i++) {
                    // 是否是对象
                    Object tmpObj = ((List) valTmp).get(i);
                    boolean isObj = tmpObj.toString().startsWith("{");
                    if (i == 0) {
                        if (!isObj) {
                            sub.append(tmpObj.toString());
                        } else {
                            sub.append(getCheckStr((Map) tmpObj, excludeKeyList));
                        }
                    } else {
                        if (!isObj) {
                            sub.append("&").append(tmpObj.toString());
                        } else {
                            sub.append("&").append(getCheckStr((Map) tmpObj, excludeKeyList));
                        }
                    }
                }
                if (StringUtils.isNotEmpty(sub.toString())) {
                    value = sub.toString();
                }
            } else {
                value = valTmp + "";
            }

            if (StringUtils.isEmpty(value)) {
                continue;
            }

            checkStrBuilder.append(keyTmp).append("=").append(value).append("&");
        }

        String checkStr = checkStrBuilder.toString();
        if (StringUtils.isNotBlank(checkStr)) {
            checkStr = checkStr.substring(0, checkStr.length() - 1);
        }
        return checkStr;
    }

    /**
     * md5编码，返回编码为均为大写编码字符
     *
     * @param origin origin
     */
    private static String md5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            //忽略
        }
        return resultString.toUpperCase();
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    // 发起post请求
    public static String doPostJson(String url, String json) {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        CloseableHttpResponse closeableHttpResponse = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            closeableHttpResponse = closeableHttpClient.execute(httpPost);
            resultString = EntityUtils.toString(closeableHttpResponse.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (closeableHttpResponse != null) {
                    closeableHttpResponse.close();
                }
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }
}
