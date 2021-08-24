package com.ofl.promotion.manage.organize.mapper;

import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import com.ofl.promotion.manage.organize.entity.filter.AdsOfflineOrganizeFilter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/20 0:51
 */
@Repository
public interface IAdsOfflineOrganizeMapper {
    /**
     * 查询全部
     */
    List<AdsOfflineOrganize> findAll(AdsOfflineOrganizeFilter filter);

    /**
     * 查询单个
     */
    AdsOfflineOrganize findOne(AdsOfflineOrganizeFilter filter);

    /**
     * 新增
     */
    Long addOflOrg(AdsOfflineOrganizeFilter filter);

    int updateOrg(AdsOfflineOrganizeFilter organizeFilter);

    Integer orgCount(AdsOfflineOrganizeFilter filter);
}
