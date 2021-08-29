package com.ofl.promotion.manage.emp.service;

import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.emp.entity.AdsOfflineEmp;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpFilter;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpMapFilter;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpMapFilter;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 12:16
 */
public interface IAdsOfflineEmpMapService {


    ResultDto<Void> queryLead(AdsOfflineEmpMapFilter filter);

    int addEmpMap(AdsOfflineEmpMapFilter empMapFilter);

    ResultDto<List<AdsOfflineEmp>> findOrgEmp(AdsOfflineEmpFilter empFilter);
}
