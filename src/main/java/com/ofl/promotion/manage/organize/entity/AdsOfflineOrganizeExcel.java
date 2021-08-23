package com.ofl.promotion.manage.organize.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr.quan
 * @Date 2021/8/23 20:40
 */

@Data
public class AdsOfflineOrganizeExcel {

    private String oneLevel;//一级

    private String twoLevel; //二级

    private String threeLevel; //三级

    private String fourLevel;//四级

    private String storeName;//门店名称

    private String guideName;//导购名称

    private String guidePhone;//手机号
}
