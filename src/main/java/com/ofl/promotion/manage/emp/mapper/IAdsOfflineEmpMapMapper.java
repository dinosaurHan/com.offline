package com.ofl.promotion.manage.emp.mapper;

import com.ofl.promotion.manage.emp.entity.AdsOfflineEmp;
import com.ofl.promotion.manage.emp.entity.AdsOfflineEmpMap;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpFilter;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpMapFilter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 15:11
 */
public interface IAdsOfflineEmpMapMapper {

    List<AdsOfflineEmpMap> findLead(AdsOfflineEmpMapFilter empMap);

    List<AdsOfflineEmp> findEmpMapByOrgId(AdsOfflineEmpFilter empFilter);

    int add(AdsOfflineEmpMapFilter empMapFilter);

}
