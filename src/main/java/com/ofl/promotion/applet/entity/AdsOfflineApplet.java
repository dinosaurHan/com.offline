package com.ofl.promotion.applet.entity;

import com.ofl.promotion.common.entity.AdsOfflineBaseParam;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/25 20:43
 */
@Data
public class AdsOfflineApplet extends AdsOfflineBaseParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long guideId;

    private String qrCode;

    private List<Integer> openBusiness;


}
