package com.ofl.promotion.manage.store.entity;

import lombok.Data;

/**
 * @Author Mr.quan
 * @Date 2021/8/27 0:08
 */
@Data
public class AdsOfflineStoreVo extends AdsOfflineStore {

    private String oneLevel;//一级

    private String twoLevel; //二级

    private String threeLevel; //三级

    private String fourLevel;//四级

    private String storeName;//门店名称

}
