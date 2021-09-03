package com.ofl.promotion.manage.data.service;

import com.github.pagehelper.PageInfo;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.data.entity.AdsOfflineDouyinData;
import com.ofl.promotion.manage.data.entity.filter.AdsOfflineDataFilter;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/27 19:01
 */
public interface IAdsOfflineDataService {

    ResultDto<PageInfo<AdsOfflineDouyinData>> query(AdsOfflineDataFilter filter);

    ResultDto<PageInfo<AdsOfflineDouyinData>> queryDetail(AdsOfflineDataFilter filter);

    ResultDto<Void> export(AdsOfflineDataFilter filter);

    ResultDto<Void> importDetail(AdsOfflineDataFilter filter);

    ResultDto<Void> exportDetail(AdsOfflineDataFilter filter);

    ResultDto<Void> queryConfig(AdsOfflineDataFilter filter);

    ResultDto<Void> importConfig(AdsOfflineDataFilter filter);

    ResultDto<Void> exportConfig(AdsOfflineDataFilter filter);
}
