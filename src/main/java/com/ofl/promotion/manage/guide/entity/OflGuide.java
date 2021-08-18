package com.ofl.promotion.manage.guide.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OflGuide {
    private Long guideId;
    private String guideName;
    private Long phone;
    private Long storeId;
    private Integer delFlag;
    private Long insertTime;
    private Long updateTime;

}
