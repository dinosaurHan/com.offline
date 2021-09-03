package com.ofl.promotion.manage.data.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr.quan
 * @Date 2021/8/27 19:47
 */
@Data
public class AdsOfflineDouyinData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long id;
    /**
     * 抖音id
     */
    private String dyId;
    /**
     * 一级
     */
    private String firstLevel;
    /**
     * 二级
     */
    private String secondLevel;
    /**
     * 三级
     */
    private String thirdLevel;
    /**
     * 四级
     */
    private String fourLevel;
    /**
     * 导购id
     */
    private Long guideId;
    /**
     * 导购手机号
     */
    private String guidePhone;
    /**
     * 拉新数
     */
    private Long pullNewCount;
    /**
     * 无效数量
     */
    private Long invPullNewCount;
    /**
     * 导购名称
     */
    private String phone;
    /**
     * 无效数量
     */
    private String storeName;

    /**
     * 导购名称
     */
    private String guideName;

    /**
     * 时间
     */
    private String date;
}
