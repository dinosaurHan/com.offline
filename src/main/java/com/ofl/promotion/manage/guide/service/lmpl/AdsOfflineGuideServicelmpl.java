package com.ofl.promotion.manage.guide.service.lmpl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ofl.promotion.applet.entity.filter.AdsOfflineAppletFilter;
import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.common.enums.OrgStatusEnum;
import com.ofl.promotion.common.utils.DateUtils;
import com.ofl.promotion.common.utils.ExcelUtils;
import com.ofl.promotion.manage.guide.entity.AdsOfflineGuide;
import com.ofl.promotion.manage.guide.entity.AdsOfflineGuideAuth;
import com.ofl.promotion.manage.guide.entity.AdsOfflineGuideVo;
import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;
import com.ofl.promotion.manage.guide.mapper.IAdsOfflineGuideMapper;
import com.ofl.promotion.manage.guide.service.IAdsOfflineGuideService;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import com.ofl.promotion.manage.organize.entity.filter.AdsOfflineOrganizeFilter;
import com.ofl.promotion.manage.organize.service.IAdsOflOrganizeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 12:22
 */
@Slf4j
@Service
public class AdsOfflineGuideServicelmpl implements IAdsOfflineGuideService {

    @Autowired
    private IAdsOfflineGuideMapper adsOfflineGuideMapper;

    @Autowired
    private IAdsOflOrganizeService adsOflOrganizeService;

    private static final String COMMA = ",";

    //excel标题
    private static final String[] GUIDE_TITLE = {"一级","二级","三级","四级","门店名称","导购名称","电话号码","导购ID","上次操作时间","状态"};

    private static final String GUIDE_FILE_NAME = "导购表";

    @Override
    public ResultDto<Integer> guideCount(AdsOfflineGuideFilter filter) {
        try{
            if (StringUtils.isBlank(filter.getAncestorIds())) {
                log.error("ancestorIds is empty");
                return new ResultDto<>(Constant.Code.FAIL,"ancestorIds is empty");
            }

            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,adsOfflineGuideMapper.guideCount(filter));
        }catch (Exception e){
            log.error("guideCount fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public void export(AdsOfflineGuideFilter filter, HttpServletResponse response) {
        try{
            ResultDto<PageInfo<AdsOfflineGuideVo>> pageInfoResultDto = queryGuide(filter);
            if (pageInfoResultDto.getRet() != Constant.Code.SUCC){
                log.error("export query guide fail ret:{}|msg:{}",pageInfoResultDto.getRet(),pageInfoResultDto.getMsg());
                return ;
            }

            //获取数据
            List<AdsOfflineGuideVo> guideVoList = getAdsOfflineGuideResult(pageInfoResultDto);

            //excel文件名
             String fileName = GUIDE_FILE_NAME+System.currentTimeMillis()+".xls";

            Object[] objects = guideVoList.toArray();
            //创建HSSFWorkbook
            HSSFWorkbook wb = ExcelUtils.export(GUIDE_FILE_NAME, GUIDE_TITLE, objects);

            //响应到客户端
            setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
            return ;
        }catch (Exception e){
            log.error("guide export",e);
            return;
        }
    }

    private List<AdsOfflineGuideVo> getAdsOfflineGuideResult(ResultDto<PageInfo<AdsOfflineGuideVo>> pageInfoResultDto) {
        List<AdsOfflineGuideVo> guideVoList = pageInfoResultDto.getData().getList();
        for (AdsOfflineGuideVo adsOfflineGuideVo : guideVoList) {
            //获取状态名称 1-启用 2-停用
            if (adsOfflineGuideVo.getStatus() != null){
                adsOfflineGuideVo.setStatusName(OrgStatusEnum.getStatusName(adsOfflineGuideVo.getStatus()));
            }

            //时间戳转换成string
            if (adsOfflineGuideVo.getUpdateTime() != null){
                adsOfflineGuideVo.setStrUpdateTime(DateUtils.conversionTime(adsOfflineGuideVo.getUpdateTime()));
            }
        }
        return guideVoList;
    }

    @Override
    public ResultDto<PageInfo<AdsOfflineGuideVo>> queryGuide(AdsOfflineGuideFilter filter) {

        try{
            if (filter.getOrganizeId() == null || filter.getPage() == 0 || filter.getPageSize() == 0){
                log.error("organizeId || page || pageSize is empty");
                return new ResultDto<>(Constant.Code.FAIL,"organizeId || page || pageSize is empty");
            }

            //判断该机构是否存在
            AdsOfflineOrganizeFilter organizeFilter = new AdsOfflineOrganizeFilter();
            organizeFilter.setOrganizeId(filter.getOrganizeId());
            ResultDto<AdsOfflineOrganize> organizeResult = adsOflOrganizeService.queryOrg(organizeFilter);
            if (organizeResult.getRet() != Constant.Code.SUCC || organizeResult.getData() == null) {
                log.error("queryOrg fail:{}", JSON.toJSONString(organizeFilter));
                return new ResultDto<>(Constant.Code.FAIL,"没有该机构");
            }

            //查询筛选条件下的机构
            AdsOfflineOrganizeFilter queryOrg = new AdsOfflineOrganizeFilter();
            queryOrg.setAncestorIds(organizeResult.getData().getAncestorIds() + COMMA + filter.getOrganizeId());
            queryOrg.setOrganizeName(filter.getOrganizeName());
            queryOrg.setOrganizeLevel(filter.getOrganizeLevel());
            ResultDto<AdsOfflineOrganize> queryOrgRel = adsOflOrganizeService.queryOrg(queryOrg);
            if (queryOrgRel.getRet() != Constant.Code.SUCC || queryOrgRel.getData() == null) {
                log.error("queryOrg fail:{}", JSON.toJSONString(organizeFilter));
                return new ResultDto<>(Constant.Code.FAIL,"没有该机构");
            }

            //查询导购
            AdsOfflineGuideFilter queryFilter = new AdsOfflineGuideFilter();
            queryFilter.setAncestorIds(queryOrgRel.getData().getAncestorIds());
            queryFilter.setGuideName(filter.getGuideName());
            queryFilter.setStatus(filter.getStatus());
            queryFilter.setPhone(filter.getPhone());
            //设置分页
            PageHelper.startPage(filter.getPage(),filter.getPageSize());
            List<AdsOfflineGuideVo> guideList = adsOfflineGuideMapper.findAll(queryFilter);

            //添加上级信息
            addOrgHightLevel(guideList);

            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,new PageInfo<>(guideList));
        }catch (Exception e){
            log.error("guide query fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }

    }

    @Override
    public ResultDto<AdsOfflineGuide> findOne(AdsOfflineGuideFilter filter) {
        try{
            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,adsOfflineGuideMapper.findOne(filter));
        }catch (Exception e){
            log.error("guide findOne fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<AdsOfflineGuideAuth> findGuideDyAuth(AdsOfflineGuideFilter filter) {
        try{
            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,adsOfflineGuideMapper.findGuideDyAuth(filter));
        }catch (Exception e){
            log.error("guide findOne fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);

        }
    }

    @Override
    public ResultDto<Void> batchUpdGuideStatus(AdsOfflineGuideFilter filter) {
        try{

            if (CollectionUtils.isEmpty(filter.getGuideIdList())){
                log.error("guideIdList is empty");
                return new ResultDto<>();
            }

            for (Long guideId : filter.getGuideIdList()) {
                filter.setGuideId(guideId);
                if (adsOfflineGuideMapper.update(filter) < 0){
                    log.error("batchUpdGuideStatus fail param:{}",JSON.toJSONString(filter));
                }
            }

            return new ResultDto<>();
        }catch (Exception e){
            log.error("batchUpdGuideStatus fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }

    }

    @Override
    public ResultDto<Void> delGuide(AdsOfflineGuideFilter filter) {
        try{
            if (CollectionUtils.isEmpty(filter.getGuideIdList())){
                log.error("guideIdList is empty");
                return new ResultDto<>();
            }

            for (Long guideId : filter.getGuideIdList()) {
                filter.setGuideId(guideId);
                filter.setDelFlag(Constant.DelFlag.INVALID);
                if (adsOfflineGuideMapper.update(filter) < 0){
                    log.error("batchUpdGuideStatus fail param:{}",JSON.toJSONString(filter));
                }
            }
        }catch (Exception e){
            log.error("delGuide fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
        return null;
    }

    private void addOrgHightLevel(List<AdsOfflineGuideVo> guideList) {
        for (AdsOfflineGuideVo adsOfflineGuideVo : guideList) {
            String ancestorIds = adsOfflineGuideVo.getAncestorIds();
            if (StringUtils.isBlank(ancestorIds)){
                continue;
            }
            //查询上级信息
            AdsOfflineOrganizeFilter organize = new AdsOfflineOrganizeFilter();
            //剔除超级管理员层级
            String subAncestorIds = adsOfflineGuideVo.getAncestorIds().substring(4,ancestorIds.length());
            organize.setParentIds(subAncestorIds + COMMA +adsOfflineGuideVo.getOrganizeId());
            ResultDto<List<AdsOfflineOrganize>> organizeResultDto = adsOflOrganizeService.queryHigherLevel(organize);
            if (organizeResultDto.getRet() != Constant.Code.SUCC || CollectionUtils.isEmpty(organizeResultDto.getData())){
                log.error("queryHigherLevel fail:{}", JSON.toJSONString(organize));
                return;
            }

            int level = 1;
            for (AdsOfflineOrganize offlineOrganize : organizeResultDto.getData()) {
                if (StringUtils.isBlank(offlineOrganize.getOrganizeName())){
                    continue;
                }

                if (level == 1){
                    adsOfflineGuideVo.setOneLevel(offlineOrganize.getOrganizeName());
                }

                if (level == 2){
                    adsOfflineGuideVo.setTwoLevel(offlineOrganize.getOrganizeName());
                }

                if (level == 3){
                    adsOfflineGuideVo.setThreeLevel(offlineOrganize.getOrganizeName());
                }

                if (level == 4){
                    adsOfflineGuideVo.setFourLevel(offlineOrganize.getOrganizeName());
                }

                level++;
            }
        }
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
