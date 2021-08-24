package com.ofl.promotion.manage.store.controller;

import com.ofl.promotion.manage.store.entity.filter.AdsOfflineStoreFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Mr.quan
 * @Date 2021/8/18 22:07
 */
@RestController
@RequestMapping("/manage/store")
public class AdsOfflineStoreController {

    @RequestMapping("/export")
    public void export(AdsOfflineStoreFilter filter){

    }
}
