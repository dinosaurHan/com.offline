package com.ofl.promotion.store;

import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.guide.entity.OflGuide;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
public class AdsStoreController {

    @RequestMapping(value = "list")
    public ResultDto<OflGuide> selectAll(){
        return new ResultDto<>();
    }
}
