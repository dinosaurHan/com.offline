package com.ofl.promotion.manage.organize.entity;

import com.ofl.promotion.common.entity.AdsOfflineRequestParam;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr.quan
 * @Date 2021/8/19 23:54
 */
@Data
public class AdsOfflineOrganize extends AdsOfflineRequestParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机构id(自增）
     */
    private Long organizeId;
    /**
     * 上级机构id(自增）
     */
    private Long parentId;
    /**
     * 机构名称
     */
    private String organizeName;
    /**
     * 祖先id
     */
    private String ancestorIds;
    /**
     * 层级
     */
    private Integer organizeLevel;
    /**
     * 机构状态（1-正常；2-停用）
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
