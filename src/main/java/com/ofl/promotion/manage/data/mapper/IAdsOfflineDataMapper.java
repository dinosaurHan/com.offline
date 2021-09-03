package com.ofl.promotion.manage.data.mapper;

import com.ofl.promotion.manage.data.entity.AdsOfflineDouyinData;
import com.ofl.promotion.manage.data.entity.filter.AdsOfflineDataFilter;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/27 19:05
 */
public interface IAdsOfflineDataMapper {

    List<AdsOfflineDouyinData> findAll(AdsOfflineDataFilter filter);

    List<AdsOfflineDouyinData> count(AdsOfflineDataFilter filter);
}
