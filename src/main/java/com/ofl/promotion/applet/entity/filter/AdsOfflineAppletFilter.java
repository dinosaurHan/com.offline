package com.ofl.promotion.applet.entity.filter;

import com.ofl.promotion.applet.entity.AdsOfflineApplet;
import lombok.Data;

/**
 * @Author Mr.quan
 * @Date 2021/8/25 20:43
 */
@Data
public class AdsOfflineAppletFilter extends AdsOfflineApplet {

    private static final long serialVersionUID = 1L;

    //微信解析参数
    private String iv;

    private String encryptedData;

    private String code;

}
