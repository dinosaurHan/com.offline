package com.ofl.promotion.manage.store.entity;

import lombok.Data;

/**
 * @Author Mr.quan
 * @Date 2021/8/27 0:08
 */
@Data
public class AdsOfflineStoreVo {

    private String oneLevel;//一级

    private String twoLevel; //二级

    private String threeLevel; //三级

    private String fourLevel;//四级

    private String storeName;//门店名称

    private String strUpdateTime;//更新时间

    private String statusName;

    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 机构id
     */
    private Long organizeId;
    /**
     * 状态
     */
    private Integer status;
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

    private String ancestorIds;
}
