package com.ofl.promotion.manage.emp.service.lmpl;

import com.alibaba.fastjson.JSON;
import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.common.utils.JwtUtils;
import com.ofl.promotion.common.utils.SMSUtils;
import com.ofl.promotion.manage.emp.entity.AdsOfflineEmp;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpFilter;
import com.ofl.promotion.manage.emp.mapper.IAdsOfflineEmpMapper;
import com.ofl.promotion.manage.emp.service.IAdsOfflineEmpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 12:17
 */
@Service
@Slf4j
public class AdsOfflineEmpServicelmpl implements IAdsOfflineEmpService {

    @Autowired
    private IAdsOfflineEmpMapper adsOfflineEmpMapper;

    @Override
    public ResultDto<AdsOfflineEmp> findOne(AdsOfflineEmpFilter filter) {
        try {
            if (StringUtils.isBlank(filter.getPhone())){
                log.error("findOne param invalid:{}", JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL,"");
            }

            return new ResultDto<>(Constant.Code.SUCC,null,adsOfflineEmpMapper.findOne(filter));
        } catch (Exception e){
            log.error("addLowerLevel fail",e);
            return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Long> addEmp(AdsOfflineEmpFilter filter) {
        try{
            if (StringUtils.isBlank(filter.getPhone()) || StringUtils.isBlank(filter.getEmpName())){
                log.error("findOne param invalid:{}", JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL,"phone || name is empty");
            }

            return new ResultDto<>(Constant.Code.SUCC,null,adsOfflineEmpMapper.add(filter));
        }catch (Exception e){
            log.error("addLowerLevel fail",e);
            return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Void> updateEmp(AdsOfflineEmpFilter adsOfflineEmpFilter) {
        try{
            if (adsOfflineEmpFilter.getEmpId() == null){
                log.error("empId is empty param:{}",JSON.toJSONString(adsOfflineEmpFilter));
                return new ResultDto<>(Constant.Code.FAIL,"empId is empty");
            }

            int  a = adsOfflineEmpMapper.update(adsOfflineEmpFilter);
            return new ResultDto<>();
        } catch (Exception e){
            log.error("updateEmp fail",e);
            return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<List<AdsOfflineEmp>> findAll(AdsOfflineEmpFilter filter) {
        try{

        } catch (Exception e){
            log.error("findAll emp fail",e);
            return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.SYSTEM_ERROR);
        }
        return null;
    }

    @Override
    public ResultDto<Object> login(AdsOfflineEmpFilter filter) {
        try{
            if (StringUtils.isBlank(filter.getPhone()) || StringUtils.isBlank(filter.getIdentifyCode())){
                log.error("phone || identifyCode is empty");
                return new ResultDto<>(Constant.Code.FAIL,"phone || identifyCode is empty");
            }

            //校验验证码


            //获取token
            String token = JwtUtils.createJWT(filter.getPhone(), 24 * 60 * 60 * 1000);

            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,token);
        } catch (Exception e){
            log.error("emp login fail",e);
            return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Object> getIdentifyingCode(AdsOfflineEmpFilter filter) {
        try{
            if (StringUtils.isBlank(filter.getPhone()) || filter.getPhone().length() != 11){
                log.error("phone:{} is invalid",filter.getPhone());
                return new ResultDto<>(Constant.Code.FAIL,"手机号验证失败,请重新检查你的手机号是否正确");
            }

            ResultDto<String> smsResult = SMSUtils.sendSMS(filter.getPhone());
            if (smsResult.getRet() != Constant.Code.SUCC){
                log.error("sendSMS fail ret:{},msg:{}",smsResult.getRet(),smsResult.getMsg());
                return new ResultDto<>(smsResult.getRet(),smsResult.getMsg());
            }
            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,smsResult.getData());
        } catch (Exception e){
            log.error("emp getIdentifyingCode fail",e);
            return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

}
