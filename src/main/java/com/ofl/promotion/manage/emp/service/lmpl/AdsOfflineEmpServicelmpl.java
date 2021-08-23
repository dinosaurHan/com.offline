package com.ofl.promotion.manage.emp.service.lmpl;

import com.alibaba.fastjson.JSON;
import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.ResultDto;
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
            if (StringUtils.isBlank(filter.getPhone()) || StringUtils.isBlank(filter.getName())){
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

        return null;
    }

    @Override
    public ResultDto<List<AdsOfflineEmp>> findAll(AdsOfflineEmpFilter filter) {

        return null;
    }

}
