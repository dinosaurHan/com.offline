package com.ofl.promotion.aop;

import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.AdsOfflineBaseParam;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.common.utils.JwtUtils;
import com.ofl.promotion.manage.emp.entity.AdsOfflineEmp;
import com.ofl.promotion.manage.emp.entity.AdsOfflineEmpMap;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpFilter;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpMapFilter;
import com.ofl.promotion.manage.emp.service.IAdsOfflineEmpMapService;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import com.ofl.promotion.manage.organize.entity.filter.AdsOfflineOrganizeFilter;
import com.ofl.promotion.manage.organize.service.IAdsOflOrganizeService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* 操作权限校验
 **/
@Aspect
@Configuration
@Order(value= Ordered.HIGHEST_PRECEDENCE + 20)
@Slf4j
public class OperateAuthAspects {

    @Autowired
    private IAdsOflOrganizeService adsOflOrganizeService;

    @Autowired
    private IAdsOfflineEmpMapService adsOfflineEmpMapService;

    @Around("@annotation(operateAuth)")
    public Object operateAuth(ProceedingJoinPoint pjp, OperateAuth operateAuth) throws Throwable{
        //获取登录类型

        AdsOfflineBaseParam param = new AdsOfflineBaseParam();
        try {
            Object[] args = pjp.getArgs();
            for (Object object : args) {
                if (object instanceof AdsOfflineBaseParam){
                    param = (AdsOfflineBaseParam)object;
                    break;
                }
            }

            //判断是否是机构负责人
            AdsOfflineEmpMapFilter empFilter = new AdsOfflineEmpMapFilter();
            empFilter.setPhone(param.getBasePhone());
            ResultDto<List<AdsOfflineEmpMap>> leadResult = adsOfflineEmpMapService.findLead(empFilter);
            if (leadResult.getRet() != Constant.Code.SUCC || CollectionUtils.isEmpty(leadResult.getData())){
                log.error("find org emp ret:{}|msg:{}|data:{}",leadResult.getRet(),leadResult.getMsg(),leadResult.getData());
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.NO_PERMISSION_FAIL);
            }

            //查看操作的机构上级
            AdsOfflineOrganizeFilter organizeFilter = new AdsOfflineOrganizeFilter();
            organizeFilter.setOrganizeId(param.getOrganizeId());
            ResultDto<AdsOfflineOrganize> organizeResultDto = adsOflOrganizeService.queryOrg(organizeFilter);
            if (organizeResultDto.getRet() != Constant.Code.SUCC || organizeResultDto.getData() == null){
                log.error("operateAuth queryOrg ret:{}|msg:{}|data:{}",organizeResultDto.getRet(),organizeResultDto.getMsg(),organizeResultDto.getData());
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.NO_PERMISSION_FAIL);
            }

            AdsOfflineOrganize offlineOrganize = organizeResultDto.getData();
            if (StringUtils.isBlank(offlineOrganize.getAncestorIds())){
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.NO_PERMISSION_FAIL);
            }

            String[] splitOrgIds = offlineOrganize.getAncestorIds().split(",");
            if (splitOrgIds.length == 0){
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.NO_PERMISSION_FAIL);
            }
            List<String> orgList = Arrays.asList(splitOrgIds);
            if (!orgList.contains(leadResult.getData().get(0).getOrganizeId())){
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.NO_PERMISSION_FAIL);
            }

        } catch (SignatureException | MalformedJwtException e){
            //token illegality
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
            //获取token中用户手机号
            param.setBasePhone(claims.getSubject());
        }

        //4.process on
        return pjp.proceed();
    }

}
