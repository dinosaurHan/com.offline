package com.ofl.promotion.manage.organize.entity.dto;

import com.ofl.promotion.manage.organize.entity.AdsOfflineOrgLead;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import lombok.Data;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/24 0:35
 */
@Data
public class AdsOfflineOrganizeDto extends AdsOfflineOrganize {
    /**
     * 负责人
     */
    private List<AdsOfflineOrgLead> leadList;
}
