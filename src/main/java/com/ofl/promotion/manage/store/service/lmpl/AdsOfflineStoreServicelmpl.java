package com.ofl.promotion.manage.store.service.lmpl;

import com.alibaba.fastjson.JSON;
import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;
import com.ofl.promotion.manage.store.entity.filter.AdsOfflineStoreFilter;
import com.ofl.promotion.manage.store.mapper.IAdsOfflineStoreMapper;
import com.ofl.promotion.manage.store.service.IAdsOfflineStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 12:15
 */
@Service
@Slf4j
public class AdsOfflineStoreServicelmpl implements IAdsOfflineStoreService {

    @Autowired
    private IAdsOfflineStoreMapper adsOfflineStoreMapper;

    @Override
    public ResultDto<Long> addStore(AdsOfflineStoreFilter filter) {
        try{
            if (StringUtils.isBlank(filter.getStoreName()) || filter.getOrganizeId() == null){
                log.error("addStore param invalid:{}", JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL,"storeName || organizeId is empty");
            }

            Long storeId = adsOfflineStoreMapper.add(filter);
            if (storeId <= 0){
                log.error("addStore add fail param:{}",JSON.toJSONString(filter));
                return new ResultDto<>();
            }

            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SUCC,storeId);
        }catch (Exception e){
            log.error("addStore fail",e);
            return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Integer> storeCount(AdsOfflineStoreFilter filter) {
        try{
            if (StringUtils.isBlank(filter.getAncestorIds())){
                log.error("ancestorIds param invalid:{}", JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL,"ancestorIds is empty");
            }

            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,adsOfflineStoreMapper.storeCount(filter));
        }catch (Exception e){
            log.error("storeCount fail",e);
            return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Void> export(AdsOfflineStoreFilter filter) {

        return null;
    }

    @Override
    public ResultDto<Object> queryStore(AdsOfflineGuideFilter filter) {
        try{
            if (filter.getOrganizeId() == null){
                log.error("organizeId is empty");
                return new ResultDto<>(Constant.Code.FAIL,"organizeId is empty");
            }

            //判断该机构是否存在
//            AdsOfflineOrganize offlineOrganize = adsOfflineOrganizeMapper.findOne(filter);
//            if (offlineOrganize == null || StringUtils.isBlank(offlineOrganize.getAncestorIds())) {
//                return new ResultDto<>(Constant.Code.FAIL,"没有该机构");
//            }

            //查询导购


            //获取上级信息


        }catch (Exception e){
            log.error("store query fail",e);
        }
        return null;
    }
}
