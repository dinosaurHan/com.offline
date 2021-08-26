package com.ofl.promotion.manage.store.service;

import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;
import com.ofl.promotion.manage.store.entity.filter.AdsOfflineStoreFilter;

/**
 * @Author Mr.quan
 * @Date 2021/8/18 22:14
 */
public interface IAdsOfflineStoreService {

    /**
     * 添加门店
     */
    ResultDto<Long> addStore(AdsOfflineStoreFilter filter);

    ResultDto<Integer> storeCount(AdsOfflineStoreFilter filter);

    ResultDto<Void> export(AdsOfflineStoreFilter filter);

    ResultDto<Object> queryStore(AdsOfflineGuideFilter filter);
}