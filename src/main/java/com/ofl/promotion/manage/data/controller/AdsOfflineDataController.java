package com.ofl.promotion.manage.data.controller;


import com.github.pagehelper.PageInfo;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.data.entity.AdsOfflineDouyinData;
import com.ofl.promotion.manage.data.entity.filter.AdsOfflineDataFilter;
import com.ofl.promotion.manage.data.service.IAdsOfflineDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/27 19:01
 */
@RequestMapping("/manage/data")
@RestController
public class AdsOfflineDataController {

    @Autowired
    private IAdsOfflineDataService adsOfflineDataService;

    @RequestMapping("/query")
    public ResultDto<PageInfo<AdsOfflineDouyinData>> query(AdsOfflineDataFilter filter){
        return adsOfflineDataService.query(filter);
    }

}
