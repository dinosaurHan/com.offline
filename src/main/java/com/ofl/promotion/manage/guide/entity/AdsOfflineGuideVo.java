package com.ofl.promotion.manage.guide.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr.quan
 * @Date 2021/8/27 0:08
 */
@Data
public class AdsOfflineGuideVo extends AdsOfflineGuide {

    private String oneLevel;//一级

    private String twoLevel; //二级

    private String threeLevel; //三级

    private String fourLevel;//四级

    private String storeName;//门店名称

}
