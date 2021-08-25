package com.ofl.promotion.applet.service;

import com.ofl.promotion.applet.entity.AdsOfflineApplet;
import com.ofl.promotion.applet.entity.filter.AdsOfflineAppletFilter;
import com.ofl.promotion.common.entity.ResultDto;

/**
 * @Author Mr.quan
 * @Date 2021/8/18 22:31
 */
public interface IAdsOfflineAppletService {

    ResultDto<AdsOfflineApplet> getUserInfo(AdsOfflineAppletFilter filter);

    ResultDto<AdsOfflineApplet> getUserDetalInfo(AdsOfflineAppletFilter filter);

    ResultDto<AdsOfflineApplet> getBusinessData(AdsOfflineAppletFilter filter);

    ResultDto<String> login(AdsOfflineAppletFilter filter);
}
