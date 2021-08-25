package com.ofl.promotion.applet.service.lmpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ofl.promotion.applet.entity.AdsOfflineApplet;
import com.ofl.promotion.applet.entity.filter.AdsOfflineAppletFilter;
import com.ofl.promotion.applet.service.IAdsOfflineAppletService;
import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.common.utils.JwtUtils;
import com.ofl.promotion.common.utils.WechatUtils;
import com.ofl.promotion.manage.guide.service.IAdsOfflineGuideService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author Mr.quan
 * @Date 2021/8/18 22:34
 */
@Slf4j
@Service
public class AdsOfflineAppletServicelmpl implements IAdsOfflineAppletService {

    @Autowired
    private IAdsOfflineGuideService adsOfflineGuideService;

//    @Autowired
//    private RedisTemplate redisTemplate;

    @Override
    public ResultDto<String> login(AdsOfflineAppletFilter filter) {
        try {
            //校验微信入参
            if (StringUtils.isBlank(filter.getCode()) || StringUtils.isBlank(filter.getEncryptedData())
                    || StringUtils.isBlank(filter.getIv())){
                log.error("user login code || encryptedData || iv is empty  param:{}", JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL,"code || encryptedData || iv is empty");
            }

            //获取微信secretKey
            JSONObject sessionKey = WechatUtils.getSessionKey(filter.getCode());
            String secretKey = sessionKey.getString("session_key");
            if (StringUtils.isBlank(secretKey)){
                log.error("get secretKey fail:{}",sessionKey);
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.LOGIN_FAIL);
            }

            //解析用户手机号码
            JSONObject phoneResult = WechatUtils.getPhoneNumber(filter.getEncryptedData(), secretKey, filter.getIv());
            String purePhoneNumber = phoneResult.getString("purePhoneNumber");
            if (StringUtils.isBlank(purePhoneNumber)){
                log.error("get user phone fail:{}",phoneResult);
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.LOGIN_FAIL);
            }

            //jwt生成token  失效单位：毫秒
            String token = JwtUtils.createJWT(purePhoneNumber, 60 * 60 * 1000);
            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,token);
        }catch (Exception e){
            log.error("user login fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<AdsOfflineApplet> getUserInfo(AdsOfflineAppletFilter filter) {
        try{
            if (StringUtils.isBlank(filter.getBasePhone())){
                log.error("getUserInfo phone is empty");
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.PARAM_INVALID_FAIL);
            }

            //查询导购id

            //查询开通业务

            //获取二维码

            return new ResultDto<>();
        }catch (Exception e){
            log.error("getUserInfo fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<AdsOfflineApplet> getUserDetalInfo(AdsOfflineAppletFilter filter) {
        try{
            return new ResultDto<>();
        }catch (Exception e){
            log.error("getUserDetalInfo fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<AdsOfflineApplet> getBusinessData(AdsOfflineAppletFilter filter) {
        try{
            return new ResultDto<>();
        }catch (Exception e){
            log.error("getUserDetalInfo fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

}
