package com.ofl.promotion.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.quan
 * @Date 2021/8/25 21:44
 */
@Slf4j
public class WechatUtils {

    private static final String APP_SECRET="";
    private static final String APP_ID = "";

    public static JSONObject getSessionKey(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + APP_ID + "&secret="
                + APP_SECRET + "&js_code=" + code + "&grant_type=authorization_code";
        String reusult = GET(url);
        JSONObject oppidObj = JSONObject.parseObject(reusult);
        String openid = (String) oppidObj.get("openid");
        String session_key = (String) oppidObj.get("session_key");
        oppidObj.put("openid", openid);
        oppidObj.put("session_key", session_key);
        if (oppidObj.containsKey("unionid")) {
            oppidObj.put("unionid", (String) oppidObj.get("unionid"));
        }else{
            oppidObj.put("unionid", "");
        }
        return oppidObj;
    }

    /**
     * 微信解析手机号
     */
    public static JSONObject getPhoneNumber(String encryptedData, String session_key, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.getDecoder().decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.getDecoder().decode(session_key);
        // 偏移量
        byte[] ivByte = Base64.getDecoder().decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            log.error("weChat getPhoneNumber fail:{}",e);
        }
        return null;
    }

    //发起get请求的方法。
    public static String GET(String url) {
        String result = "";
        BufferedReader in = null;
        InputStream is = null;
        InputStreamReader isr = null;
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.connect();
            Map<String, List<String>> map = conn.getHeaderFields();
            is = conn.getInputStream();
            isr = new InputStreamReader(is);
            in = new BufferedReader(isr);
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.error("weChat url get fail",e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (is != null) {
                    is.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (Exception e) {
                log.error("weChat url get fail",e);
            }
        }
        return result;
    }
}
