package com.ofl.promotion.manage.guide.mapper;

import com.ofl.promotion.manage.guide.entity.AdsOfflineGuide;
import com.ofl.promotion.manage.guide.entity.AdsOfflineGuideVo;
import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 12:24
 */
public interface IAdsOfflineGuideMapper {

    Integer guideCount(AdsOfflineGuideFilter filter);

    List<AdsOfflineGuideVo> findAll(AdsOfflineGuideFilter queryFilter);
}
