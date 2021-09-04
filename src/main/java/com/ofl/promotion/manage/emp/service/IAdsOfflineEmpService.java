package com.ofl.promotion.manage.emp.service;

import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.emp.entity.AdsOfflineEmp;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpFilter;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpMapFilter;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 12:16
 */
public interface IAdsOfflineEmpService {

    ResultDto<AdsOfflineEmp> findOne(AdsOfflineEmpFilter filter);

    ResultDto<Void> addEmp(AdsOfflineEmpFilter filter);

    ResultDto<Void> updateEmp(AdsOfflineEmpFilter filter);

    ResultDto<List<AdsOfflineEmp>> findAll(AdsOfflineEmpFilter filter);

    ResultDto<Object> login(AdsOfflineEmpFilter filter);

    ResultDto<Void> getIdentifyingCode(AdsOfflineEmpFilter filter);
}
