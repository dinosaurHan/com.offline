package com.ofl.promotion.manage.emp.mapper;

import java.util.List;

import com.ofl.promotion.manage.emp.entity.AdsOfflineEmp;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpFilter;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpMapFilter;
import org.apache.ibatis.annotations.Param;

/** 
 * Created on 2021年08月21日
 */
public interface IAdsOfflineEmpMapper {
	
	/**
	* 查询总数量
//	*/
//	Long queryCountOflEmp(@Param("entity") AdsOfflineEmp entity);
//
//	/**
//	* 查询单个实体
//	*/
//	AdsOfflineEmp queryOflEmpById(@Param("id") Long id, @Param("queryFields") List queryFields);
	
//	/**
//	* 新增
//	*/
//	int addOflEmp(AdsOfflineEmp entity);
//
//	/**
//	* 修改
//	*/
//	int updateOflEmp(AdsOfflineEmp entity);
//
//	/**
//	* 批量删除
//	*/
//	int removeOflEmpByIds(List code);

	AdsOfflineEmp findOne(AdsOfflineEmpFilter filter);

	Long add(AdsOfflineEmpFilter filter);

    int update(AdsOfflineEmpFilter adsOfflineEmpFilter);
}
