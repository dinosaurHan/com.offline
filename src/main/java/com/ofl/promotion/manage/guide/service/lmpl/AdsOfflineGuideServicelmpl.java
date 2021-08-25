package com.ofl.promotion.manage.guide.service.lmpl;

import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;
import com.ofl.promotion.manage.guide.mapper.IAdsOfflineGuideMapper;
import com.ofl.promotion.manage.guide.service.IAdsOfflineGuideService;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 12:22
 */
@Slf4j
@Service
public class AdsOfflineGuideServicelmpl implements IAdsOfflineGuideService {

    @Autowired
    private IAdsOfflineGuideMapper adsOfflineGuideMapper;

    @Override
    public ResultDto<Integer> guideCount(AdsOfflineGuideFilter filter) {
        try{
            if (StringUtils.isBlank(filter.getAncestorIds())) {
                log.error("ancestorIds is empty");
                return new ResultDto<>(Constant.Code.FAIL,"ancestorIds is empty");
            }

            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,adsOfflineGuideMapper.guideCount(filter));
        }catch (Exception e){
            log.error("guideCount fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Void> export(AdsOfflineGuideFilter filter) {

        return null;
    }

    @Override
    public ResultDto<Object> queryGuide(AdsOfflineGuideFilter filter) {

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
            log.error("guide query fail",e);
        }
        return null;
    }
}
