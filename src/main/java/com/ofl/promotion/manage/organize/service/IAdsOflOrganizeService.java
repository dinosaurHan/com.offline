package com.ofl.promotion.manage.organize.service;

import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import com.ofl.promotion.manage.organize.entity.dto.AdsOfflineOrganizeDto;
import com.ofl.promotion.manage.organize.entity.filter.AdsOfflineOrganizeFilter;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/18 22:14
 */
public interface IAdsOflOrganizeService {

    ResultDto<AdsOfflineOrganize> addLowerLevel(AdsOfflineOrganizeFilter filter);

    ResultDto<AdsOfflineOrganize> importExcel(MultipartFile file, Long organizeId);

    ResultDto<Void> updOrgnize(AdsOfflineOrganizeFilter filter);

    ResultDto<AdsOfflineOrganizeDto> query(AdsOfflineOrganizeFilter filter);

    ResultDto<List<AdsOfflineOrganize>> queryOrgTree(AdsOfflineOrganizeFilter filter);

    ResultDto<Object> queryLowerOrg(AdsOfflineOrganizeFilter filter);

    ResultDto<Object> countOrg(AdsOfflineOrganizeFilter filter);

    /**
     * 根据organizeId查询机构信息
     */
    ResultDto<AdsOfflineOrganize> queryOrg(AdsOfflineOrganizeFilter filter);

    ResultDto<List<AdsOfflineOrganize>> queryHigherLevel(AdsOfflineOrganizeFilter filter);

    ResultDto<Void> batchUpdOrgStatus(AdsOfflineOrganizeFilter filter);

    ResultDto<Void> delOrg(AdsOfflineOrganizeFilter filter);

    ResultDto<Void> export(AdsOfflineOrganizeFilter filter, HttpServletResponse response);
}
