package com.ofl.promotion.common.entity;

import lombok.Data;

/**
 * @Author Mr.quan
 * @Date 2021/8/24 23:50
 */
@Data
public class AdsOfflineBaseParam {

    private String token;

    private String basePhone;

    private int loginType;

    private Long organizeId;
}
