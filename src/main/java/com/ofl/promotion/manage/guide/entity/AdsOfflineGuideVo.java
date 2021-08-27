package com.ofl.promotion.manage.guide.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr.quan
 * @Date 2021/8/27 0:08
 */
@Data
public class AdsOfflineGuideVo {

    private String oneLevel;//一级

    private String twoLevel; //二级

    private String threeLevel; //三级

    private String fourLevel;//四级

    private String storeName;//门店名称

    private String guideName;//导购名称

    private String phone;//导购手机号码

    private Long guideId;//导购id

    private String strUpdateTime;//更新时间

    private String statusName;//状态名称

    private Integer status;

    private Long updateTime;//更新时间

    private Long storeId;//门店id

    private String ancestorIds;//上级id集合

    private Long organizeId;//组织id
}
