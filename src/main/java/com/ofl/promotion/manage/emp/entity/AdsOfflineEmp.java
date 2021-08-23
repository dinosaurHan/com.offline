package com.ofl.promotion.manage.emp.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr.quan
 * @Date 2021/8/21 11:58
 */
@Data
public class AdsOfflineEmp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人员id
     */
    private Long empId;
    /**
     * 人员名称
     */
    private String empName;
    /**
     * 手机号码
     */
    private String phone;
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
