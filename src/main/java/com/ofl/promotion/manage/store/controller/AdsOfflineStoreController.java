package com.ofl.promotion.manage.store.controller;

import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.store.entity.filter.AdsOfflineStoreFilter;
import com.ofl.promotion.manage.store.service.IAdsOfflineStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Mr.quan
 * @Date 2021/8/18 22:07
 */
@RestController
@RequestMapping("/manage/store")
public class AdsOfflineStoreController {

    @Autowired
    private IAdsOfflineStoreService adsOfflineStoreService;

    @RequestMapping("/export")
    public ResultDto<Void> export(AdsOfflineStoreFilter filter){
        return adsOfflineStoreService.export(filter);
    }
}
