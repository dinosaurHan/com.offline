package com.ofl.promotion.common.utils;

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

    private final static String SECRECT= "ONHGEBAJNE";

    private final static String USER = "OFFLINE_PROMOTION";

    /**
     * 签发JWT
     *
     * @param subject   可以是JSON数据 存放用户信息
     * @param ttlMillis
     * @return String
     */
    public static String createJWT(String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey();
        JwtBuilder builder = Jwts.builder().setId(JWT_ID).setSubject(subject) // 主题
                .setIssuer(USER) // 签发者
                .setIssuedAt(now) // 签发时间
                .signWith(signatureAlgorithm, secretKey); // 签名算法以及密匙
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);
            builder.setExpiration(expDate); // 过期时间
        }
        return builder.compact();
    }

    public static SecretKey generalKey() {
        byte[] encodedKey = Base64Utils.decodeFromString(SECRECT);
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
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
    }
}
