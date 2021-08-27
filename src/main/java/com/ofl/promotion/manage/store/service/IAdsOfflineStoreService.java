package com.ofl.promotion.manage.store.service;

import com.github.pagehelper.PageInfo;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;
import com.ofl.promotion.manage.store.entity.AdsOfflineStoreVo;
import com.ofl.promotion.manage.store.entity.filter.AdsOfflineStoreFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    void export(AdsOfflineStoreFilter filter, HttpServletResponse response);

    ResultDto<PageInfo<AdsOfflineStoreVo>> queryStore(AdsOfflineStoreFilter filter);
}
