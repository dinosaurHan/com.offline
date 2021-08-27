package com.ofl.promotion.manage.data.service.lmpl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.data.entity.AdsOfflineDouyinData;
import com.ofl.promotion.manage.data.entity.filter.AdsOfflineDataFilter;
import com.ofl.promotion.manage.data.mapper.IAdsOfflineDataMapper;
import com.ofl.promotion.manage.data.service.IAdsOfflineDataService;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import com.ofl.promotion.manage.organize.entity.filter.AdsOfflineOrganizeFilter;
import com.ofl.promotion.manage.organize.service.IAdsOflOrganizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/27 19:04
 */
@Slf4j
@Service
public class IAdsOfflineDataServicelmpl implements IAdsOfflineDataService {

    @Autowired
    private IAdsOfflineDataMapper adsOfflineDataMapper;

    @Autowired
    private IAdsOflOrganizeService adsOflOrganizeService;

    private static final String COMMA = ",";

    @Override
    public ResultDto<PageInfo<AdsOfflineDouyinData>> query(AdsOfflineDataFilter filter) {
        try{
            if (filter.getPage() == 0 || filter.getPageSize() == 0 || filter.getOrganizeId() == null){
                log.error("page || pageSize || organizeId is empty");
                return new ResultDto<>(Constant.Code.FAIL,"page || pageSize || organizeId is empty");
            }

            //判断该机构是否存在
            AdsOfflineOrganizeFilter organizeFilter = new AdsOfflineOrganizeFilter();
            organizeFilter.setOrganizeId(filter.getOrganizeId());
            ResultDto<AdsOfflineOrganize> organizeResult = adsOflOrganizeService.queryOrg(organizeFilter);
            if (organizeResult.getRet() != Constant.Code.SUCC || organizeResult.getData() == null) {
                log.error("queryOrg fail:{}", JSON.toJSONString(organizeFilter));
                return new ResultDto<>(Constant.Code.FAIL,"没有该机构");
            }

            //设置分页
            PageHelper.startPage(filter.getPage(),filter.getPageSize());
            filter.setAncestorIds(organizeResult.getData().getAncestorIds() + COMMA + filter.getOrganizeId());
            List<AdsOfflineDouyinData> douyinData = adsOfflineDataMapper.findAll(filter);
            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,new PageInfo<>(douyinData));
        }catch (Exception e){
            log.error("data query fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }
}
