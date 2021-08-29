package com.ofl.promotion.manage.guide.entity.filter;

import com.ofl.promotion.manage.guide.entity.AdsOfflineGuide;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/24 22:15
 */
@Data
public class AdsOfflineGuideFilter extends AdsOfflineGuide implements Serializable {

    private int page;

    private int pageSize;

    private Long organizeId;

    private String ancestorIds;

    private List<Long> guideIdList;
}
