package com.ofl.promotion.manage.guide.controller;

import com.github.pagehelper.PageInfo;
import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.guide.entity.AdsOfflineGuide;
import com.ofl.promotion.manage.guide.entity.AdsOfflineGuideVo;
import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;
import com.ofl.promotion.manage.guide.service.IAdsOfflineGuideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Mr.quan
 * @Date 2021/8/18 22:07
 */
@RestController
@RequestMapping("/manage/guide")
@Slf4j
public class AdsOfflineGuideController {

    @Autowired
    private IAdsOfflineGuideService adsOfflineGuideService;

    /**
     * 导购导出
     */
    @RequestMapping("/export")
    public ResultDto<Void> export(@RequestBody AdsOfflineGuideFilter filter, HttpServletResponse response){
        return adsOfflineGuideService.export(filter,response);
    }

    /**
     * 导购列表查询
     */
    @RequestMapping("/query")
    public ResultDto<PageInfo<AdsOfflineGuideVo>> queryGuide(@RequestBody AdsOfflineGuideFilter filter){
        return adsOfflineGuideService.queryGuide(filter);
    }

    /**
     * 批量启用停用
     */
    @RequestMapping("/batch/upd")
    public ResultDto<Void> batchUpdGuideStatus(@RequestBody AdsOfflineGuideFilter filter){
        return adsOfflineGuideService.batchUpdGuideStatus(filter);
    }

    @RequestMapping("/del")
    public ResultDto<Void> delGuide(@RequestBody AdsOfflineGuideFilter filter){
        try{
            return adsOfflineGuideService.delGuide(filter);
        }catch (Exception e){
            log.error("delGuide fail",e);
            return new ResultDto<>(Constant.Code.FAIL,e.getMessage());
        }
    }
}
