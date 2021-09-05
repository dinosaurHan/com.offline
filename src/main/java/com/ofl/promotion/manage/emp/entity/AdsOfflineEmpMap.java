package com.ofl.promotion.manage.emp.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 15:25
 */
@Data
public class AdsOfflineEmpMap implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 映射id
     */
    private Long mapId;
    /**
     * 人员id
     */
    private Long empId;
    /**
     * 上级机构Id
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

    private String phone;

    private Integer organizeLevel;
}
