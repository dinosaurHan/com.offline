package com.ofl.promotion.applet.controller;

import com.ofl.promotion.aop.LoginAuthentication;
import com.ofl.promotion.applet.entity.AdsOfflineApplet;
import com.ofl.promotion.applet.entity.AdsOfflineAppletInfo;
import com.ofl.promotion.applet.entity.filter.AdsOfflineAppletFilter;
import com.ofl.promotion.applet.service.IAdsOfflineAppletService;
import com.ofl.promotion.common.entity.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Mr.quan
 * @Date 2021/8/18 22:30
 */
@Slf4j
@RestController
@RequestMapping("/applet")
public class AdsOfflineAppletController {

    @Autowired
    private IAdsOfflineAppletService adsAppletService;

    /**
     * 用户登录 用户即导购
     */
    @RequestMapping("/user/login")
    public ResultDto<String> login(AdsOfflineAppletFilter filter){
        return adsAppletService.login(filter);
    }

    /**
     * 获取推广信息
     */
    @RequestMapping("/promotion/getUserInfo")
    @LoginAuthentication
    public ResultDto<AdsOfflineApplet> getUserInfo(AdsOfflineAppletFilter filter){
        return adsAppletService.getUserInfo(filter);
    }

    /**
     * 获取用户详情
     */
    @RequestMapping("/personal/getUserInfo")
    @LoginAuthentication
    public ResultDto<AdsOfflineAppletInfo> getUserDetalInfo(AdsOfflineAppletFilter filter){
        return adsAppletService.getUserDetalInfo(filter);
    }

    /**
     * 获取业务数据
     */
    @RequestMapping("/business/getData")
    @LoginAuthentication
    public ResultDto<AdsOfflineApplet> getBusinessData(AdsOfflineAppletFilter filter){
        return adsAppletService.getBusinessData(filter);
    }

    /**
     * 获取业务数据
     */
    @RequestMapping("/business/getDataInfo")
    @LoginAuthentication
    public ResultDto<AdsOfflineApplet> getBusinessDataInfo(AdsOfflineAppletFilter filter){
        return adsAppletService.getBusinessDataInfo(filter);
    }
}
