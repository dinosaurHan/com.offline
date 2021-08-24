package com.ofl.promotion.manage.guide.controller;

import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;
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

    @RequestMapping("/export")
    public void export(AdsOfflineGuideFilter filter){
        
    }
}
