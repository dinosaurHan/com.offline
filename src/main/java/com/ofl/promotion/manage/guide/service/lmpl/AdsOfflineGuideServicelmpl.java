package com.ofl.promotion.manage.guide.service.lmpl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.guide.entity.AdsOfflineGuide;
import com.ofl.promotion.manage.guide.entity.AdsOfflineGuideVo;
import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;
import com.ofl.promotion.manage.guide.mapper.IAdsOfflineGuideMapper;
import com.ofl.promotion.manage.guide.service.IAdsOfflineGuideService;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import com.ofl.promotion.manage.organize.entity.filter.AdsOfflineOrganizeFilter;
import com.ofl.promotion.manage.organize.service.IAdsOflOrganizeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 12:22
 */
@Slf4j
@Service
public class AdsOfflineGuideServicelmpl implements IAdsOfflineGuideService {

    @Autowired
    private IAdsOfflineGuideMapper adsOfflineGuideMapper;

    @Autowired
    private IAdsOflOrganizeService adsOflOrganizeService;

    private static final String COMMA = ",";

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
            if (filter.getOrganizeId() == null || filter.getPage() == 0 || filter.getPageSize() == 0){
                log.error("organizeId || page || pageSize is empty");
                return new ResultDto<>(Constant.Code.FAIL,"organizeId || page || pageSize is empty");
            }

            //判断该机构是否存在
            AdsOfflineOrganizeFilter organizeFilter = new AdsOfflineOrganizeFilter();
            organizeFilter.setOrganizeId(filter.getOrganizeId());
            ResultDto<AdsOfflineOrganize> organizeResult = adsOflOrganizeService.queryOrg(organizeFilter);
            if (organizeResult.getRet() != Constant.Code.SUCC || organizeResult.getData() == null) {
                log.error("queryOrg fail:{}", JSON.toJSONString(organizeFilter));
                return new ResultDto<>(Constant.Code.FAIL,"没有该机构");
            }

            //查询导购
            AdsOfflineGuideFilter queryFilter = new AdsOfflineGuideFilter();
            queryFilter.setAncestorIds(organizeResult.getData().getAncestorIds() + COMMA + filter.getOrganizeId());
            queryFilter.setGuideName(filter.getGuideName());
            queryFilter.setStatus(filter.getStatus());
            queryFilter.setPhone(filter.getPhone());
            PageHelper.startPage(filter.getPage(),filter.getPageSize());
            List<AdsOfflineGuideVo> guideList = adsOfflineGuideMapper.findAll(queryFilter);

            //获取上级信息
            AdsOfflineOrganizeFilter organize = new AdsOfflineOrganizeFilter();
            organize.setParentIds(organizeResult.getData().getAncestorIds());
            ResultDto<List<AdsOfflineOrganize>> organizeResultDto = adsOflOrganizeService.queryHigherLevel(organize);
            if (organizeResultDto.getRet() != Constant.Code.SUCC || CollectionUtils.isEmpty(organizeResultDto.getData())){
                log.error("queryHigherLevel fail:{}", JSON.toJSONString(organize));
                return new ResultDto<>(Constant.Code.FAIL,"查询上级机构失败");
            }

            addOrgHightLevel(guideList, organizeResultDto);

            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,new PageInfo<>(guideList));
        }catch (Exception e){
            log.error("guide query fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }

    }

    private void addOrgHightLevel(List<AdsOfflineGuideVo> guideList, ResultDto<List<AdsOfflineOrganize>> organizeResultDto) {
        for (AdsOfflineGuideVo adsOfflineGuideVo : guideList) {
            int level = 1;
            for (AdsOfflineOrganize offlineOrganize : organizeResultDto.getData()) {
                if (StringUtils.isBlank(offlineOrganize.getOrganizeName())){
                    continue;
                }

                if (level == 1){
                    adsOfflineGuideVo.setOneLevel(offlineOrganize.getOrganizeName());
                }

                if (level == 2){
                    adsOfflineGuideVo.setTwoLevel(offlineOrganize.getOrganizeName());
                }

                if (level == 3){
                    adsOfflineGuideVo.setThreeLevel(offlineOrganize.getOrganizeName());
                }

                if (level == 4){
                    adsOfflineGuideVo.setFourLevel(offlineOrganize.getOrganizeName());
                }

                level++;
            }
        }
    }
}
