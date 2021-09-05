package com.ofl.promotion.manage.store.service.lmpl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.common.enums.OrgStatusEnum;
import com.ofl.promotion.common.utils.DateUtils;
import com.ofl.promotion.common.utils.ExcelUtils;
import com.ofl.promotion.manage.guide.entity.AdsOfflineGuideVo;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import com.ofl.promotion.manage.organize.entity.filter.AdsOfflineOrganizeFilter;
import com.ofl.promotion.manage.organize.service.IAdsOflOrganizeService;
import com.ofl.promotion.manage.store.entity.AdsOfflineStore;
import com.ofl.promotion.manage.store.entity.AdsOfflineStoreVo;
import com.ofl.promotion.manage.store.entity.filter.AdsOfflineStoreFilter;
import com.ofl.promotion.manage.store.mapper.IAdsOfflineStoreMapper;
import com.ofl.promotion.manage.store.service.IAdsOfflineStoreService;
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
 * @Date 2021/8/22 12:15
 */
@Service
@Slf4j
public class AdsOfflineStoreServicelmpl implements IAdsOfflineStoreService {

    @Autowired
    private IAdsOfflineStoreMapper adsOfflineStoreMapper;

    @Autowired
    private IAdsOflOrganizeService adsOflOrganizeService;

    private static final String COMMA = ",";

    //excel标题
    private static final String[] STORE_TITLE = {"一级","二级","三级","四级","门店名称","上次操作时间","状态"};

    private static final String STORE_FILE_NAME = "门店表";

    @Override
    public ResultDto<Void> addStore(AdsOfflineStoreFilter filter) {
        try{
            if (StringUtils.isBlank(filter.getStoreName()) || filter.getOrganizeId() == null){
                log.error("addStore param invalid:{}", JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL,"storeName || organizeId is empty");
            }

            Long storeId = adsOfflineStoreMapper.add(filter);
            if (storeId <= 0){
                log.error("addStore add fail param:{}",JSON.toJSONString(filter));
                return new ResultDto<>();
            }

            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC);
        }catch (Exception e){
            log.error("addStore fail",e);
            return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Integer> storeCount(AdsOfflineStoreFilter filter) {
        try{
            if (StringUtils.isBlank(filter.getAncestorIds())){
                log.error("ancestorIds param invalid:{}", JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL,"ancestorIds is empty");
            }

            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,adsOfflineStoreMapper.storeCount(filter));
        }catch (Exception e){
            log.error("storeCount fail",e);
            return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Void> export(AdsOfflineStoreFilter filter,HttpServletResponse response) {
        try{
            ResultDto<PageInfo<AdsOfflineStoreVo>> pageInfoResultDto = queryStore(filter);
            if (pageInfoResultDto.getRet() != Constant.Code.SUCC){
                log.error("export query store fail ret:{}|msg:{}",pageInfoResultDto.getRet(),pageInfoResultDto.getMsg());
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
            }

            //获取数据
            List<AdsOfflineStoreVo> storeVoList = getAdsOfflineStoreResult(pageInfoResultDto);

            //excel文件名
            String fileName = STORE_FILE_NAME + System.currentTimeMillis()+".xls";

            Object[] objects = storeVoList.toArray();
            //创建HSSFWorkbook
            HSSFWorkbook wb = ExcelUtils.export(STORE_FILE_NAME, STORE_TITLE, objects);

            //响应到客户端
            ExcelUtils.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
            return null;
        }catch (Exception e){
            log.error("guide export",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<PageInfo<AdsOfflineStoreVo>> queryStore(AdsOfflineStoreFilter filter) {
        try{
            if (filter.getOrganizeId() == null){
                log.error("organizeId is empty");
                return new ResultDto<>(Constant.Code.FAIL,"organizeId is empty");
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

            //查询门店信息
            AdsOfflineStoreFilter store = new AdsOfflineStoreFilter();
            store.setAncestorIds(queryOrgRel.getData().getAncestorIds());
            store.setStoreName(filter.getStoreName());
            store.setStatus(filter.getStatus());
            PageHelper.startPage(filter.getPage(),filter.getPageSize());
            List<AdsOfflineStoreVo> storeVoList = adsOfflineStoreMapper.findAllByOrgIds(store);

            //添加上级信息
            addOrgHightLevel(storeVoList);
            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,new PageInfo<>(storeVoList));
        }catch (Exception e){
            log.error("store query fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Void> batchUpdStoreStatus(AdsOfflineStoreFilter filter) {
        try{
            if (CollectionUtils.isEmpty(filter.getStoreIdList())){
                return new ResultDto<>();
            }

            //修改门店状态
            for (Long storeId : filter.getStoreIdList()) {
                filter.setStoreId(storeId);
                if (adsOfflineStoreMapper.update(filter) < 0){
                    log.error("batchUpdStoreStatus fail param:{}",JSON.toJSONString(filter));
                }
            }

            return new ResultDto<>();
        }catch (Exception e){
            log.error("batchUpdStoreStatus fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Void> delStore(AdsOfflineStoreFilter filter) {
        try{
            if (CollectionUtils.isEmpty(filter.getStoreIdList())){
                return new ResultDto<>();
            }

            //修改门店状态
            for (Long storeId : filter.getStoreIdList()) {
                filter.setStoreId(storeId);
                filter.setDelFlag(Constant.DelFlag.INVALID);
                if (adsOfflineStoreMapper.update(filter) < 0){
                    log.error("batchUpdStoreStatus fail param:{}",JSON.toJSONString(filter));
                }
            }
        }catch (Exception e){
            log.error("delStore fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
        return null;
    }

    @Override
    public ResultDto<AdsOfflineStore> findOne(AdsOfflineStoreFilter storeFilter) {
        try{
            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,adsOfflineStoreMapper.findOne(storeFilter));
        }catch (Exception e){
            log.error("findOne fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    private void addOrgHightLevel(List<AdsOfflineStoreVo>  storeVoList) {
        for (AdsOfflineStoreVo adsOfflineStoreVo : storeVoList) {
            String ancestorIds = adsOfflineStoreVo.getAncestorIds();
            if (StringUtils.isBlank(ancestorIds)){
                continue;
            }

            if (adsOfflineStoreVo.getAncestorIds().length() < 4){
                continue;
            }

            //查询所有上级信息
            AdsOfflineOrganizeFilter organize = new AdsOfflineOrganizeFilter();
            //剔除超级管理员层级
            String subAncestorIds = adsOfflineStoreVo.getAncestorIds().substring(4,ancestorIds.length());
            organize.setParentIds(subAncestorIds + COMMA + adsOfflineStoreVo.getOrganizeId());
            ResultDto<List<AdsOfflineOrganize>> organizeResultDto = adsOflOrganizeService.queryHigherLevel(organize);
            if (organizeResultDto.getRet() != Constant.Code.SUCC || CollectionUtils.isEmpty(organizeResultDto.getData())){
                log.error("queryHigherLevel fail:{}", JSON.toJSONString(organize));
                return;
            }

            int level = 1;
            //封装上级信息
            for (AdsOfflineOrganize offlineOrganize : organizeResultDto.getData()) {
                if (StringUtils.isBlank(offlineOrganize.getOrganizeName())){
                    continue;
                }
                //一级
                if (level == 1){
                    adsOfflineStoreVo.setOneLevel(offlineOrganize.getOrganizeName());
                }
                //二级
                if (level == 2){
                    adsOfflineStoreVo.setTwoLevel(offlineOrganize.getOrganizeName());
                }
                //三级
                if (level == 3){
                    adsOfflineStoreVo.setThreeLevel(offlineOrganize.getOrganizeName());
                }
                //四级
                if (level == 4){
                    adsOfflineStoreVo.setFourLevel(offlineOrganize.getOrganizeName());
                }

                level++;
            }
        }
    }

    private List<AdsOfflineStoreVo> getAdsOfflineStoreResult(ResultDto<PageInfo<AdsOfflineStoreVo>> pageInfoResultDto) {
        List<AdsOfflineStoreVo> storeVoList = pageInfoResultDto.getData().getList();
        for (AdsOfflineStoreVo adsOfflineStoreVo : storeVoList) {
            //获取状态名称 1-启用 2-停用
            if (adsOfflineStoreVo.getStatus() != null){
                adsOfflineStoreVo.setStatusName(OrgStatusEnum.getStatusName(adsOfflineStoreVo.getStatus()));
            }

            //时间戳转换成string
            if (adsOfflineStoreVo.getUpdateTime() != null){
                adsOfflineStoreVo.setStrUpdateTime(DateUtils.conversionTime(adsOfflineStoreVo.getUpdateTime()));
            }
        }
        return storeVoList;
    }

}
