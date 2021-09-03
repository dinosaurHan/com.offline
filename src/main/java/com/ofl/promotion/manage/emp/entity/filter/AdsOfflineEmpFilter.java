package com.ofl.promotion.manage.emp.entity.filter;

import com.ofl.promotion.manage.emp.entity.AdsOfflineEmp;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import lombok.Data;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/19 23:56
 */
@Data
public class AdsOfflineEmpFilter extends AdsOfflineEmp implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String phone;

    private String identifyCode;

    private Long organizeId;
}
