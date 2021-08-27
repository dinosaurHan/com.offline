package com.ofl.promotion.manage.store.mapper;

import com.ofl.promotion.manage.store.entity.AdsOfflineStoreVo;
import com.ofl.promotion.manage.store.entity.filter.AdsOfflineStoreFilter;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 12:12
 */
public interface IAdsOfflineStoreMapper {

    Long add(AdsOfflineStoreFilter filter);

    int storeCount(AdsOfflineStoreFilter filter);

    List<AdsOfflineStoreVo> findAllByOrgIds(AdsOfflineStoreFilter store);
}
