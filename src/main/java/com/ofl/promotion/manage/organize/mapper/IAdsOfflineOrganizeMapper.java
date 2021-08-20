package com.ofl.promotion.manage.organize.mapper;

import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import com.ofl.promotion.manage.organize.entity.filter.AdsOfflineOrganizeFilter;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/20 0:51
 */
public interface IAdsOfflineOrganizeMapper {

    List<AdsOfflineOrganize> query(AdsOfflineOrganizeFilter filter);
}
