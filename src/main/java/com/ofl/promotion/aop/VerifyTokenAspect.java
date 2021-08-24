package com.ofl.promotion.aop;

import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.AdsOfflineRequestParam;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Date;

/**
* 校验Token
 **/
@Aspect
@Configuration
@Order(value= Ordered.HIGHEST_PRECEDENCE + 20)
@Slf4j
public class VerifyTokenAspect {

//    @Resource
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Resource
//    private RedisTemplate redisTemplate;

    @Around("@annotation(verifyToken)")
    public Object verifyToken(ProceedingJoinPoint pjp, VerifyToken verifyToken) throws Throwable{
        Object[] args = pjp.getArgs();
        AdsOfflineRequestParam param = null;
        for (Object object : args) {
            if (object instanceof AdsOfflineRequestParam){
                param = (AdsOfflineRequestParam)object;
                break;
            }
        }

        //获取token
        String token = param.getToken();
        if (StringUtils.isBlank(token)){
            log.error("token is blank");
            throw new RuntimeException("校验失败");
        }
        try {
            Claims claims = JwtUtils.parseJWT(token);
            Date expiration = claims.getExpiration();
            //当前时间大于设置的过期时间，失效
            if (new Date().getTime() > expiration.getTime()){
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.TOKEN_INVALID);
            }
        } catch (SignatureException | MalformedJwtException e){
            //token illegality
            log.warn("The token is illegality:",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.LOGIN_FAIL);
        } catch (ExpiredJwtException e){
            log.error("Verify Token Exception:",e);
            Claims claims = e.getClaims();
            System.out.println(claims.getId());
            Date expiration = claims.getExpiration();
            //当前时间大于设置的过期时间，失效
            if (new Date().getTime() > expiration.getTime()){
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.TOKEN_INVALID);
            }

        }

        //4.process on
        return pjp.proceed();
    }
}
