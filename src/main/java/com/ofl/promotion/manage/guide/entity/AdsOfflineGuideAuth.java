package com.ofl.promotion.manage.guide.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr.quan
 * @Date 2021/8/27 23:55
 */
@Data
public class AdsOfflineGuideAuth extends AdsOfflineGuide implements Serializable {

    private String dyId;
}
