package com.ofl.promotion.manage.guide.controller;

import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;
import com.ofl.promotion.manage.guide.service.IAdsOfflineGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Mr.quan
 * @Date 2021/8/18 22:07
 */
@RestController
@RequestMapping("/manage/guide")
public class AdsOfflineGuideController {

    @Autowired
    private IAdsOfflineGuideService adsOfflineGuideService;

    /**
     * 导购导出
     */
    @RequestMapping("/export")
    public ResultDto<Void> export(AdsOfflineGuideFilter filter){
        return adsOfflineGuideService.export(filter);
    }

    /**
     * 导购列表查询
     */
    @RequestMapping("/query")
    public ResultDto<Object> queryGuide(AdsOfflineGuideFilter filter){
        return adsOfflineGuideService.queryGuide(filter);
    }

}
