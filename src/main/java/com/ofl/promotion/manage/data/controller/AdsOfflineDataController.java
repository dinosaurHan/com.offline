package com.ofl.promotion.manage.data.controller;


import com.github.pagehelper.PageInfo;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.data.entity.AdsOfflineDouyinData;
import com.ofl.promotion.manage.data.entity.filter.AdsOfflineDataFilter;
import com.ofl.promotion.manage.data.service.IAdsOfflineDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResultDto<PageInfo<AdsOfflineDouyinData>> query(@RequestBody AdsOfflineDataFilter filter){
        return adsOfflineDataService.query(filter);
    }

    @RequestMapping("/detail/query")
    public ResultDto<PageInfo<AdsOfflineDouyinData>> queryDetail(@RequestBody AdsOfflineDataFilter filter){
        return adsOfflineDataService.queryDetail(filter);
    }

    @RequestMapping("/export")
    public ResultDto<Void> export(@RequestBody AdsOfflineDataFilter filter){
        return adsOfflineDataService.export(filter);
    }

    @RequestMapping("/detail/import")
    public ResultDto<Void> importDetail(@RequestBody AdsOfflineDataFilter filter){
        return adsOfflineDataService.importDetail(filter);
    }

    @RequestMapping("/detail/export")
    public ResultDto<Void> exportDetail(@RequestBody AdsOfflineDataFilter filter){
        return adsOfflineDataService.exportDetail(filter);
    }

    @RequestMapping("/config/query")
    public ResultDto<PageInfo<AdsOfflineDouyinData>> queryConfig(@RequestBody AdsOfflineDataFilter filter){
        return adsOfflineDataService.queryConfig(filter);
    }

    @RequestMapping("/config/import")
    public ResultDto<Void> importConfig(@RequestBody AdsOfflineDataFilter filter){
        return adsOfflineDataService.importConfig(filter);
    }

    @RequestMapping("/config/export")
    public ResultDto<Void> exportConfig(@RequestBody AdsOfflineDataFilter filter){
        return adsOfflineDataService.exportConfig(filter);
    }
}
