package com.ofl.promotion.manage.store.controller;

import com.github.pagehelper.PageInfo;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;
import com.ofl.promotion.manage.store.entity.AdsOfflineStoreVo;
import com.ofl.promotion.manage.store.entity.filter.AdsOfflineStoreFilter;
import com.ofl.promotion.manage.store.service.IAdsOfflineStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/18 22:07
 */
@RestController
@RequestMapping("/manage/store")
public class AdsOfflineStoreController {

    @Autowired
    private IAdsOfflineStoreService adsOfflineStoreService;

    /**
     * 门店查询（分页）
     */
    @RequestMapping("/query")
    public ResultDto<PageInfo<AdsOfflineStoreVo>> queryStore(@RequestBody AdsOfflineStoreFilter filter){
        return adsOfflineStoreService.queryStore(filter);
    }

    /**
     * 门店导出
     */
    @RequestMapping("/export")
    public ResultDto<Void> export(@RequestBody AdsOfflineStoreFilter filter, HttpServletResponse response){
        return adsOfflineStoreService.export(filter, response);
    }

    /**
     * 批量启用停用
     */
    @RequestMapping("/batch/upd")
    public ResultDto<Void> batchUpdStatus(@RequestBody AdsOfflineStoreFilter filter){
        return adsOfflineStoreService.batchUpdStoreStatus(filter);
    }

    @RequestMapping("/del")
    public ResultDto<Void> delStore(@RequestBody AdsOfflineStoreFilter filter){
        return adsOfflineStoreService.delStore(filter);
    }
}
