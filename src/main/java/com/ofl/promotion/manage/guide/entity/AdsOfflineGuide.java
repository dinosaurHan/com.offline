package com.ofl.promotion.manage.guide.entity;

import com.ofl.promotion.common.entity.AdsOfflineRequestParam;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr.quan
 * @Date 2021/8/24 22:02
 */
@Data
public class AdsOfflineGuide extends AdsOfflineRequestParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 导购id
     */
    private Long guideId;
    /**
     * 导购名称
     */
    private String guideName;
    /**
     * 导购手机号码
     */
    private Long phone;
    /**
     * 门店id
     */
    private Long storeId;
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
}
