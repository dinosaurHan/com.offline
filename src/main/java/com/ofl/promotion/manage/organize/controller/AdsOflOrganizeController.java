package com.ofl.promotion.manage.organize.controller;

import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import com.ofl.promotion.manage.organize.entity.filter.AdsOfflineOrganizeFilter;
import com.ofl.promotion.manage.organize.service.IAdsOflOrganizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/18 22:18
 */
@RestController
@RequestMapping("/manage/organize")
@Slf4j
public class AdsOflOrganizeController {

    @Autowired
    private IAdsOflOrganizeService adsOflOrganizeService;

    /**
     * 添加下级机构
     */
    @RequestMapping("/lower/level/add")
    public ResultDto<AdsOfflineOrganize> addLowerLevel(AdsOfflineOrganizeFilter filter){
        return adsOflOrganizeService.addLowerLevel(filter);
    }

    /**
     * 新增组织架构 excel
     */
    @RequestMapping("/excel/import")
    public ResultDto<AdsOfflineOrganize> importExcel(MultipartFile file, @RequestParam Long organizeId){
        return adsOflOrganizeService.importExcel(file,organizeId);
    }

    /**
     * 编辑机构
     */
    @RequestMapping("/upd")
    public ResultDto<Void> updOrgnize(AdsOfflineOrganizeFilter filter){
        return adsOflOrganizeService.updOrgnize(filter);
    }

    /**
     * 组织机构信息查询(含负责人信息)
     */
    @RequestMapping("/query")
    public ResultDto<Void> query(AdsOfflineOrganizeFilter filter){
        return adsOflOrganizeService.query(filter);
    }

    /**
     * 组织机构信息查询(含负责人信息)
     */
    @RequestMapping("/tree/query")
    public ResultDto<List<AdsOfflineOrganize>> queryOrgTree(AdsOfflineOrganizeFilter filter){
        return adsOflOrganizeService.queryOrgTree(filter);
    }

}