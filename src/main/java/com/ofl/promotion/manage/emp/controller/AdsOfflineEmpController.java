package com.ofl.promotion.manage.emp.controller;

import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.emp.entity.AdsOfflineEmpMap;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpFilter;
import com.ofl.promotion.manage.emp.service.IAdsOfflineEmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Mr.quan
 * @Date 2021/8/23 1:29
 */
@RestController
@RequestMapping("manage/user")
public class AdsOfflineEmpController {

    @Autowired
    private IAdsOfflineEmpService adsOfflineEmpService;

    /**
     * 用户登录
     */
    @RequestMapping("/login")
    public ResultDto<Object> login(@RequestBody AdsOfflineEmpFilter filter){
        return adsOfflineEmpService.login(filter);
    }

    /**
     * 获取验证码
     */
    @RequestMapping("/getCode")
    public ResultDto<Void> getIdentifyingCode(@RequestBody AdsOfflineEmpFilter filter){
        return adsOfflineEmpService.getIdentifyingCode(filter);
    }

    @RequestMapping("/getUserInfo")
    public ResultDto<AdsOfflineEmpMap> getUserInfo(@RequestBody AdsOfflineEmpFilter filter){
        return adsOfflineEmpService.getUserInfo(filter);
    }
}
