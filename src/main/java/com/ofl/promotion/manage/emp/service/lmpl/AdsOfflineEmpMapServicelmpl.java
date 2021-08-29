package com.ofl.promotion.manage.emp.service.lmpl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.emp.entity.AdsOfflineEmp;
import com.ofl.promotion.manage.emp.entity.AdsOfflineEmpMap;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpFilter;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpMapFilter;
import com.ofl.promotion.manage.emp.mapper.IAdsOfflineEmpMapMapper;
import com.ofl.promotion.manage.emp.service.IAdsOfflineEmpMapService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 15:13
 */
@Slf4j
@Service
public class AdsOfflineEmpMapServicelmpl implements IAdsOfflineEmpMapService {

    @Autowired
    private IAdsOfflineEmpMapMapper adsOfflineEmpMapMapper;

    @Override
    public ResultDto<Void> queryLead(AdsOfflineEmpMapFilter filter) {
        try{
            if (CollectionUtils.isEmpty(filter.getLeadList()) || filter.getOrganizeId() == null){
                log.error("findLead param is invalid:{}");
                return new ResultDto<>(Constant.Code.FAIL,"leadList is empty");
            }

            //封装所有新增负责人的手机号
            List<String> phoneList = Lists.newArrayList();
            for (AdsOfflineEmpFilter empFilter : filter.getLeadList()) {
                phoneList.add(empFilter.getPhone());
            }
            //查询新增所有的负责人信息是否存在
            String phones = StringUtils.strip(JSON.toJSONString(phoneList), "[]");
            AdsOfflineEmpMapFilter empMap = new AdsOfflineEmpMapFilter();
            empMap.setPhones(phones);
            List<AdsOfflineEmpMap> offlineEmps = adsOfflineEmpMapMapper.queryLead(empMap);
            if (CollectionUtils.isEmpty(offlineEmps)){
                return new ResultDto<>();
            }

            //查询同一归属层级下的所有的负责人负责的机构是否包含负责的现有的机构
            String[] orgIds = filter.getAncestorIds().split(",");
            List<String> orgIdList = Arrays.asList(orgIds);
            for (AdsOfflineEmpMap offlineEmpMap : offlineEmps) {

                if (offlineEmpMap.getOrganizeId() == null){
                    continue;
                }

                //新增负责人负责的机构是否包含现在的机构，如果是，则该整个层级有
                if (orgIdList.contains(String.valueOf(offlineEmpMap.getOrganizeId()))){
                    return new ResultDto<>(Constant.Code.FAIL,"该负责人已经是该层级的负责人，不能重复添加");
                }
            }

        }catch (Exception e){
            log.error("addLowerLevel fail",e);
            return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.SYSTEM_ERROR);
        }
        return new ResultDto<>();
    }

    @Override
    public int addEmpMap(AdsOfflineEmpMapFilter empMapFilter) {

        return 0;
    }

    @Override
    public ResultDto<List<AdsOfflineEmp>> findOrgEmp(AdsOfflineEmpFilter empFilter) {
        try{

            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,adsOfflineEmpMapMapper.findEmpMapByOrgId(empFilter));
        }catch (Exception e){
            log.error("findOrgEmp fail",e);
            return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.SYSTEM_ERROR);
        }

    }

}
