package com.ofl.promotion.manage.data.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr.quan
 * @Date 2021/9/4 1:26
 */
@Data
public class AdsOfflineDataCount extends AdsOfflineDouyinData implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long pullNewCount;

    private Long invPullNewCount;

    private Long guideCount;
}
