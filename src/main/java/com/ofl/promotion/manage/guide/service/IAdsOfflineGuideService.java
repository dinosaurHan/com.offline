package com.ofl.promotion.manage.guide.service;

import com.github.pagehelper.PageInfo;
import com.ofl.promotion.applet.entity.filter.AdsOfflineAppletFilter;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.guide.entity.AdsOfflineGuide;
import com.ofl.promotion.manage.guide.entity.AdsOfflineGuideAuth;
import com.ofl.promotion.manage.guide.entity.AdsOfflineGuideVo;
import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 12:22
 */
public interface IAdsOfflineGuideService {

    ResultDto<Integer> guideCount(AdsOfflineGuideFilter filter);

    void export(AdsOfflineGuideFilter filter, HttpServletResponse response);

    ResultDto<PageInfo<AdsOfflineGuideVo>> queryGuide(AdsOfflineGuideFilter filter);

    ResultDto<AdsOfflineGuide> findOne(AdsOfflineGuideFilter filter);

    ResultDto<AdsOfflineGuideAuth> findGuideDyAuth(AdsOfflineGuideFilter filter);

    ResultDto<Void> batchUpdGuideStatus(AdsOfflineGuideFilter filter);

    ResultDto<Void> delGuide(AdsOfflineGuideFilter filter);

}
