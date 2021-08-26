package com.ofl.promotion.manage.organize.entity.filter;

import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpFilter;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/19 23:56
 */
@Data
public class AdsOfflineOrganizeFilter extends AdsOfflineOrganize implements Serializable {

    private static final long serialVersionUID = 1L;

    private int page;

    private int pageSize;

    private Integer lowerOrgType;

    private String lowerOrgName;

    private List<AdsOfflineEmpFilter> leadList;

    private String parentIds;
}
