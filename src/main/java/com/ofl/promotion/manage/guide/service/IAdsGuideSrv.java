package com.ofl.promotion.manage.guide.service;

import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.manage.guide.entity.OflGuide;

public interface IAdsGuideSrv {
    /**
     * 创建导购
     * @return
     */
    ResultDto<Void> create(OflGuide guide);

    /**
     * 修改导购
     * @param guide
     * @return
     */
    ResultDto<Void> delete(OflGuide guide);
}
