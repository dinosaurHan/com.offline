package com.ofl.promotion.manage.emp.entity;

import com.ofl.promotion.common.entity.AdsOfflineBaseParam;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr.quan
 * @Date 2021/8/21 11:58
 */
@Data
public class AdsOfflineEmp extends AdsOfflineBaseParam implements Serializable {

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
