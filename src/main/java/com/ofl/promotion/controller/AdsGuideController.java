package com.ofl.promotion.controller;

import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.guide.entity.OflGuide;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guide")
public class AdsGuideController {

    @RequestMapping(value = "list")
    public ResultDto<OflGuide> selectAll(){
        return new ResultDto<>();
    }
}
