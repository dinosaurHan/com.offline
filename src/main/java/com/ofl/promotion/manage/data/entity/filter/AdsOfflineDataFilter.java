package com.ofl.promotion.manage.data.entity.filter;

import com.ofl.promotion.manage.data.entity.AdsOfflineDouyinData;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr.quan
 * @Date 2021/8/27 19:56
 */
@Data
public class AdsOfflineDataFilter extends AdsOfflineDouyinData implements Serializable {

    private static final long serialVersionUID = 1L;

    private int page;

    private int pageSize;

    private Long organizeId;

    private String ancestorIds;

}
