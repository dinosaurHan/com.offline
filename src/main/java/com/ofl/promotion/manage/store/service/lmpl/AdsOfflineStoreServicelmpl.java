package com.ofl.promotion.manage.store.service.lmpl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import com.ofl.promotion.manage.organize.entity.filter.AdsOfflineOrganizeFilter;
import com.ofl.promotion.manage.organize.service.IAdsOflOrganizeService;
import com.ofl.promotion.manage.store.entity.AdsOfflineStoreVo;
import com.ofl.promotion.manage.store.entity.filter.AdsOfflineStoreFilter;
import com.ofl.promotion.manage.store.mapper.IAdsOfflineStoreMapper;
import com.ofl.promotion.manage.store.service.IAdsOfflineStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 12:15
 */
@Service
@Slf4j
public class AdsOfflineStoreServicelmpl implements IAdsOfflineStoreService {

    @Autowired
    private IAdsOfflineStoreMapper adsOfflineStoreMapper;

    @Autowired
    private IAdsOflOrganizeService adsOflOrganizeService;

    private static final String COMMA = ",";

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
    public ResultDto<Object> queryStore(AdsOfflineStoreFilter filter) {
        try{
            if (filter.getOrganizeId() == null || filter.getPage() == 0 || filter.getPageSize() == 0){
                log.error("organizeId is empty");
                return new ResultDto<>(Constant.Code.FAIL,"organizeId is empty");
            }

            //判断该机构是否存在
            AdsOfflineOrganizeFilter organizeFilter = new AdsOfflineOrganizeFilter();
            organizeFilter.setOrganizeId(filter.getOrganizeId());
            ResultDto<AdsOfflineOrganize> organizeResult = adsOflOrganizeService.queryOrg(organizeFilter);
            if (organizeResult.getRet() != Constant.Code.SUCC || organizeResult.getData() == null) {
                log.error("queryOrg fail:{}", JSON.toJSONString(organizeFilter));
                return new ResultDto<>(Constant.Code.FAIL,"没有该机构");
            }

            //查询门店信息
            AdsOfflineStoreFilter store = new AdsOfflineStoreFilter();
            store.setAncestorIds(organizeResult.getData().getAncestorIds() + COMMA + filter.getOrganizeId());
            store.setStoreName(filter.getStoreName());
            store.setStatus(filter.getStatus());
            PageHelper.startPage(filter.getPage(),filter.getPageSize());
            List<AdsOfflineStoreVo> storeVoList = adsOfflineStoreMapper.findAllByOrgIds(store);

            //获取上级信息
            AdsOfflineOrganizeFilter organize = new AdsOfflineOrganizeFilter();
            organize.setParentIds(organizeResult.getData().getAncestorIds() + COMMA + filter.getOrganizeId());
            ResultDto<List<AdsOfflineOrganize>> organizeResultDto = adsOflOrganizeService.queryHigherLevel(organize);
            if (organizeResultDto.getRet() != Constant.Code.SUCC || CollectionUtils.isEmpty(organizeResultDto.getData())){
                log.error("queryHigherLevel fail:{}", JSON.toJSONString(organize));
                return new ResultDto<>(Constant.Code.FAIL,"查询上级机构失败");
            }

            addOrgHightLevel(storeVoList,organizeResultDto);
            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,new PageInfo<>(storeVoList));
        }catch (Exception e){
            log.error("store query fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    private void addOrgHightLevel(List<AdsOfflineStoreVo>  storeVoList, ResultDto<List<AdsOfflineOrganize>> organizeResultDto) {
        for (AdsOfflineStoreVo adsOfflineStoreVo : storeVoList) {
            int level = 1;
            for (AdsOfflineOrganize offlineOrganize : organizeResultDto.getData()) {
                if (StringUtils.isBlank(offlineOrganize.getOrganizeName())){
                    continue;
                }

                if (level == 1){
                    adsOfflineStoreVo.setOneLevel(offlineOrganize.getOrganizeName());
                }

                if (level == 2){
                    adsOfflineStoreVo.setTwoLevel(offlineOrganize.getOrganizeName());
                }

                if (level == 3){
                    adsOfflineStoreVo.setThreeLevel(offlineOrganize.getOrganizeName());
                }

                if (level == 4){
                    adsOfflineStoreVo.setFourLevel(offlineOrganize.getOrganizeName());
                }

                level++;
            }
        }
    }
}
