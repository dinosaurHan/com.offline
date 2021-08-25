package com.ofl.promotion.manage.guide.service;

import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 12:22
 */
public interface IAdsOfflineGuideService {

    ResultDto<Integer> guideCount(AdsOfflineGuideFilter filter);

    ResultDto<Void> export(AdsOfflineGuideFilter filter);
}
