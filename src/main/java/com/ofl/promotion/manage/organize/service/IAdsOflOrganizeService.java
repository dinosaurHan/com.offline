package com.ofl.promotion.manage.organize.service;

import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import com.ofl.promotion.manage.organize.entity.filter.AdsOfflineOrganizeFilter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/18 22:14
 */
public interface IAdsOflOrganizeService {

    ResultDto<AdsOfflineOrganize> addLowerLevel(AdsOfflineOrganizeFilter filter);

    ResultDto<AdsOfflineOrganize> importExcel(MultipartFile file, Long organizeId);

    ResultDto<Void> updOrgnize(AdsOfflineOrganizeFilter filter);

    ResultDto<Void> query(AdsOfflineOrganizeFilter filter);

    ResultDto<List<AdsOfflineOrganize>> queryOrgTree(AdsOfflineOrganizeFilter filter);
}
