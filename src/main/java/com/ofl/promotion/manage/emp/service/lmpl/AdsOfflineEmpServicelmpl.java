package com.ofl.promotion.manage.emp.service.lmpl;

import com.alibaba.fastjson.JSON;
import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.common.utils.JwtUtils;
import com.ofl.promotion.common.utils.SMSUtils;
import com.ofl.promotion.manage.emp.entity.AdsOfflineEmp;
import com.ofl.promotion.manage.emp.entity.AdsOfflineEmpMap;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpFilter;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpMapFilter;
import com.ofl.promotion.manage.emp.mapper.IAdsOfflineEmpMapMapper;
import com.ofl.promotion.manage.emp.mapper.IAdsOfflineEmpMapper;
import com.ofl.promotion.manage.emp.service.IAdsOfflineEmpService;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import com.ofl.promotion.manage.organize.entity.filter.AdsOfflineOrganizeFilter;
import com.ofl.promotion.manage.organize.mapper.IAdsOfflineOrganizeMapper;
import com.ofl.promotion.manage.organize.service.IAdsOflOrganizeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Autowired
    private IAdsOfflineEmpMapMapper adsOfflineEmpMapMapper;

    @Autowired
    private IAdsOfflineOrganizeMapper adsOfflineOrganizeMapper;

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
    public ResultDto<Void> addEmp(AdsOfflineEmpFilter filter) {
        try{
            if (StringUtils.isBlank(filter.getPhone()) || StringUtils.isBlank(filter.getEmpName())){
                log.error("findOne param invalid:{}", JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL,"phone || name is empty");
            }

            if (adsOfflineEmpMapper.add(filter) < 0){
                log.error("add emp fail param:{}",JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL,"add emp fail");
            }

            return new ResultDto<>(Constant.Code.SUCC,null);
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
            if (StringUtils.isBlank(filter.getPhone()) || filter.getPhone().length() != 11
                    || StringUtils.isBlank(filter.getIdentifyCode())){
                log.error("phone || identifyCode is invalid");
                return new ResultDto<>(Constant.Code.FAIL,"phone || identifyCode is empty");
            }

            //查询是否存在该成员
            AdsOfflineEmpFilter empFilter = new AdsOfflineEmpFilter();
            empFilter.setPhone(filter.getPhone());
            AdsOfflineEmp offlineEmp = adsOfflineEmpMapper.findOne(empFilter);
            if (offlineEmp == null){
                log.error("user login emp is empty");
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.NO_PERMISSION_FAIL);
            }

            //获取token
            String token = JwtUtils.createJWT(filter.getPhone(), 24 * 60 * 60 * 1000,Constant.LoginType.PC);

            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,token);
        } catch (Exception e){
            log.error("emp login fail",e);
            return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Void> getIdentifyingCode(AdsOfflineEmpFilter filter) {
        try{
            if (StringUtils.isBlank(filter.getPhone()) || filter.getPhone().length() != 11){
                log.error("phone:{} is invalid",filter.getPhone());
                return new ResultDto<>(Constant.Code.FAIL,"手机号验证失败,请重新检查你的手机号是否正确");
            }

            //查询是否有该人员
            AdsOfflineEmpFilter empFilter = new AdsOfflineEmpFilter();
            empFilter.setPhone(filter.getPhone());
            AdsOfflineEmp offlineEmp = adsOfflineEmpMapper.findOne(empFilter);
            if (offlineEmp == null){
                log.error("user login emp is empty");
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.NO_PERMISSION_FAIL);
            }

            ResultDto<String> smsResult = SMSUtils.sendSMS(filter.getPhone());
            if (smsResult.getRet() != Constant.Code.SUCC){
                log.error("sendSMS fail ret:{},msg:{}",smsResult.getRet(),smsResult.getMsg());
                return new ResultDto<>(smsResult.getRet(),smsResult.getMsg());
            }
            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC);
        } catch (Exception e){
            log.error("emp getIdentifyingCode fail",e);
            return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<AdsOfflineEmpMap> getUserInfo(AdsOfflineEmpFilter filter) {
        try{
            AdsOfflineEmpMapFilter empMapFilter = new AdsOfflineEmpMapFilter();
            empMapFilter.setPhone(filter.getPhone());
            List<AdsOfflineEmpMap> lead = adsOfflineEmpMapMapper.findLead(empMapFilter);

            if (CollectionUtils.isEmpty(lead)){
                return new ResultDto<>(Constant.Code.FAIL,"没有该人员");
            }

            for (AdsOfflineEmpMap empMap : lead) {
                empMap.setPhone(filter.getPhone());
                AdsOfflineOrganizeFilter organizeFilter = new AdsOfflineOrganizeFilter();
                organizeFilter.setOrganizeId(empMap.getOrganizeId());
                AdsOfflineOrganize one = adsOfflineOrganizeMapper.findOne(organizeFilter);
                empMap.setOrganizeLevel(one.getOrganizeLevel());
            }

            return new ResultDto<>(Constant.Code.SUCC,null,lead.get(0));

        }catch (Exception e){

        }
        return new ResultDto<>();
    }

}
