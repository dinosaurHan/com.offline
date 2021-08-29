package com.ofl.promotion.common.utils;

import com.ofl.promotion.common.constant.Constant;
import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.Base64Utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.UUID;

/**
 * @Author Mr.quan
 * @Date 2021/8/17 0:54
 */
public class JwtUtils {

    private static final String JWT_ID = UUID.randomUUID().toString();

    private final static String PC_SECRECT= "PC_ONHGEBAJNE";
    private final static String APPLET_SECRECT= "APPLET_ONHGEBAJNE";

    private final static String PC_USER = "PC_OFFLINE_PROMOTION";
    private final static String APPLET_USER = "APPLET_OFFLINE_PROMOTION";

    /**
     * 签发JWT
     *
     * @param subject   可以是JSON数据 存放用户信息
     * @param ttlMillis
     * @return String
     */
    public static String createJWT(String subject, long ttlMillis,int loginType) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey(loginType);
        if (secretKey == null){
            return null;
        }

        //根据不同的登录类型获取不同的签发者和密钥
        JwtBuilder builder = Jwts.builder();
        //小程序
        if (loginType == Constant.LoginType.APPLET){
            builder = Jwts.builder().setId(JWT_ID).setSubject(subject) // 主题
                    .setIssuer(APPLET_USER) // 签发者
                    .setIssuedAt(now) // 签发时间
                    .signWith(signatureAlgorithm, secretKey); // 签名算法以及密匙
        }else if (loginType == Constant.LoginType.PC){
            builder = Jwts.builder().setId(JWT_ID).setSubject(subject) // 主题
                    .setIssuer(PC_USER) // 签发者
                    .setIssuedAt(now) // 签发时间
                    .signWith(signatureAlgorithm, secretKey); // 签名算法以及密匙
        }

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);
            builder.setExpiration(expDate); // 过期时间
        }
        return builder.compact();
    }

    public static SecretKey generalKey(int loginType) {
        byte[] encodedKey = null;
        if (loginType == Constant.LoginType.APPLET){
            encodedKey = Base64Utils.decodeFromString(APPLET_SECRECT);
        }else if (loginType == Constant.LoginType.PC){
            encodedKey = Base64Utils.decodeFromString(PC_SECRECT);
        }else {
            encodedKey = null;
        }
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 解析JWT字符串
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt,int loginType) throws Exception {
        SecretKey secretKey = generalKey(loginType);
        if (secretKey == null){
            return null;
        }
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
    }
}
