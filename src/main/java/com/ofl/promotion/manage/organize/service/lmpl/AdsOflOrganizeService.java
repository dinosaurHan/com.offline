package com.ofl.promotion.manage.organize.service.lmpl;

import com.alibaba.fastjson.JSON;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import com.ofl.promotion.manage.organize.entity.filter.AdsOfflineOrganizeFilter;
import com.ofl.promotion.manage.organize.mapper.IAdsOfflineOrganizeMapper;
import com.ofl.promotion.manage.organize.service.IAdsOflOrganizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/18 22:16
 */
@Slf4j
@Service
public class AdsOflOrganizeService implements IAdsOflOrganizeService {

    @Autowired
    private IAdsOfflineOrganizeMapper adsOfflineOrganizeMapper;

    @Override
    public ResultDto<AdsOfflineOrganize> addLowerLevel(AdsOfflineOrganizeFilter filter) {
        ResultDto<AdsOfflineOrganize> resultDto = new ResultDto<>();
        try {

        }catch (Exception e){
            log.error("addLowerLevel fail",e);
        }
        return resultDto;
    }

    @Override
    public ResultDto<AdsOfflineOrganize> importExcel(MultipartFile file, Long organizeId) {
        return null;
    }

    @Override
    public ResultDto<Void> updOrgnize(AdsOfflineOrganizeFilter filter) {
        return null;
    }

    @Override
    public ResultDto<Void> query(AdsOfflineOrganizeFilter filter) {
        return null;
    }

    @Override
    public ResultDto<List<AdsOfflineOrganize>> queryOrgTree(AdsOfflineOrganizeFilter filter) {

        List<AdsOfflineOrganize> offlineOrganizeList = adsOfflineOrganizeMapper.query(filter);
        System.out.println(JSON.toJSONString(offlineOrganizeList));
        return new ResultDto<>(0,"操作成功",offlineOrganizeList);
    }
}
