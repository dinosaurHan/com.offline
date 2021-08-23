package com.ofl.promotion.manage.store.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 11:59
 */
@Data
public class AdsOfflineStore implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 机构id
     */
    private Long organizeId;
    /**
     * 删除状态（0-正常；1-删除）
     */
    private Integer delFlag;
    /**
     * 创建时间
     */
    private Long insertTime;
    /**
     * 更新时间
     */
    private Long updateTime;
}
