package com.ofl.promotion.manage.guide.mapper;

import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 12:24
 */
public interface IAdsOfflineGuideMapper {

    Integer guideCount(AdsOfflineGuideFilter filter);
}
