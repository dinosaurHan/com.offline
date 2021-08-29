package com.ofl.promotion.manage.emp.entity.filter;

import com.ofl.promotion.manage.emp.entity.AdsOfflineEmpMap;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 15:26
 */
@Data
public class AdsOfflineEmpMapFilter extends AdsOfflineEmpMap implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<AdsOfflineEmpFilter> leadList;

    private String phones;

    private String phone;

    private String ancestorIds;

}
