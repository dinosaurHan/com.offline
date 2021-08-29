package com.ofl.promotion.manage.organize.service.lmpl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.ofl.promotion.common.constant.Constant;
import com.ofl.promotion.common.entity.ResultDto;
import com.ofl.promotion.common.utils.ExcelUtils;
import com.ofl.promotion.manage.emp.entity.AdsOfflineEmp;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpFilter;
import com.ofl.promotion.manage.emp.entity.filter.AdsOfflineEmpMapFilter;
import com.ofl.promotion.manage.emp.service.IAdsOfflineEmpMapService;
import com.ofl.promotion.manage.guide.entity.filter.AdsOfflineGuideFilter;
import com.ofl.promotion.manage.guide.service.IAdsOfflineGuideService;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrgLead;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganize;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganizeCount;
import com.ofl.promotion.manage.organize.entity.dto.AdsOfflineOrganizeDto;
import com.ofl.promotion.manage.organize.entity.AdsOfflineOrganizeExcel;
import com.ofl.promotion.manage.organize.entity.filter.AdsOfflineOrganizeFilter;
import com.ofl.promotion.manage.store.entity.AdsOfflineStore;
import com.ofl.promotion.manage.store.entity.filter.AdsOfflineStoreFilter;
import com.ofl.promotion.manage.organize.mapper.IAdsOfflineOrganizeMapper;
import com.ofl.promotion.manage.emp.service.IAdsOfflineEmpService;
import com.ofl.promotion.manage.store.service.IAdsOfflineStoreService;
import com.ofl.promotion.manage.organize.service.IAdsOflOrganizeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.quan
 * @Date 2021/8/18 22:16
 */
@Slf4j
@Service
public class AdsOflOrganizeServicelmpl implements IAdsOflOrganizeService {

    @Autowired
    private IAdsOfflineOrganizeMapper adsOfflineOrganizeMapper;

    @Autowired
    private IAdsOfflineStoreService adsOfflineStoreService;

    @Autowired
    private IAdsOfflineEmpService adsOfflineEmpService;

    @Autowired
    private IAdsOfflineEmpMapService adsOfflineEmpMapService;

    @Autowired
    private IAdsOfflineGuideService adsOfflineGuideService;

    private static final String COMMA = ",";

    @Override
    @Transactional
    public ResultDto<AdsOfflineOrganize> addLowerLevel(AdsOfflineOrganizeFilter filter) {
        try {
            //校验入参
            if (filter.getOrganizeId() == null || filter.getLowerOrgType() == null ||
                    StringUtils.isBlank(filter.getLowerOrgName())) {
                log.error("addLowerLevel organizeId || orgType || orgName is Empty param:{}",JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL, "organizeId || orgType || orgName is Empty");
            }

            //判断该机构是否存在
            AdsOfflineOrganize offlineOrganize = adsOfflineOrganizeMapper.findOne(filter);
            if (offlineOrganize == null){
                return new ResultDto<>(Constant.Code.FAIL,"没有该机构");
            }

            //判断下级是否冲突
            if (checkLowerLevelOrgConflict(filter)){
                return new ResultDto<>(Constant.Code.FAIL, Constant.ResultMsg.LOWER_LEVEL_ORG_TYPE_CONFLICT);
            }

            //是否已经成为机构负责人
            if (!CollectionUtils.isEmpty(filter.getLeadList())){
                AdsOfflineEmpMapFilter empMapFilter = new AdsOfflineEmpMapFilter();
                empMapFilter.setLeadList(filter.getLeadList());
                empMapFilter.setAncestorIds(offlineOrganize.getAncestorIds());
                //判断是否已经成为负责人（同层级不能重复添加机构负责人）
                ResultDto<Void> leadResult=  adsOfflineEmpMapService.queryLead(empMapFilter);
                if (leadResult.getRet() != Constant.Code.SUCC){
                    return new ResultDto<>(leadResult.getRet(),leadResult.getMsg());
                }
            }

            //判断该机构名称是否存在
            AdsOfflineOrganizeFilter organize = new AdsOfflineOrganizeFilter();
            organize.setOrganizeName(filter.getLowerOrgName());
            AdsOfflineOrganize adsOfflineOrganize = adsOfflineOrganizeMapper.findOne(organize);
            if (adsOfflineOrganize != null){
                return new ResultDto<>(Constant.Code.FAIL,"该机构名称已经被创建");
            }

            //添加组织架构
            filter.setParentId(filter.getOrganizeId());
            filter.setOrganizeLevel(offlineOrganize.getOrganizeLevel() + 1);
            filter.setAncestorIds(offlineOrganize.getAncestorIds() + COMMA + offlineOrganize.getOrganizeId());
            ResultDto<AdsOfflineOrganize> addResult = addLowerLevelOrgOrStore(filter);
            if (addResult.getRet() != Constant.Code.SUCC) return addResult;

            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,addResult.getData());
        }catch (Exception e){
            log.error("addLowerLevel fail",e);
            throw new RuntimeException(Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    private ResultDto<AdsOfflineOrganize> addLowerLevelOrgOrStore(AdsOfflineOrganizeFilter filter) throws RuntimeException {

        //添加机构
        if (filter.getLowerOrgType() == Constant.LowerLevelType.ORGANIZE){

            //添加下级机构
            filter.setOrganizeName(filter.getLowerOrgName());
            Long addOrg = adsOfflineOrganizeMapper.addOflOrg(filter);
            if (addOrg <= 0){
                log.error("addLowerLevel addOflOrg fail:{}", JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL, "addLowerLevel addOflOrg fail");
            }

            //添加下级机构负责人,门店无需添加负责人
            if (!CollectionUtils.isEmpty(filter.getLeadList())){
                addLowerLevelLead(filter.getLeadList(),filter.getOrganizeId());
            }

        }else if (filter.getLowerOrgType() == Constant.LowerLevelType.STORE){
            //判断该名称是否已经创建
            AdsOfflineStoreFilter storeFilter = new AdsOfflineStoreFilter();
            storeFilter.setStoreName(filter.getLowerOrgName());
            ResultDto<AdsOfflineStore> storeResultDto = adsOfflineStoreService.findOne(storeFilter);
            if (storeResultDto.getRet() != Constant.Code.SUCC){
                log.error("addLowerLevelOrgOrStore findOne ret:{}|msg:{}",storeResultDto.getRet(),storeResultDto.getMsg());
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.FAIL);
            }

            if (storeResultDto.getData() != null){
                return new ResultDto<>(Constant.Code.FAIL,"该名称的门店已创建");
            }

            //添加机构下级门店
            storeFilter.setOrganizeId(filter.getParentId());
            ResultDto<Void> resultDto = adsOfflineStoreService.addStore(storeFilter);
            if (resultDto.getRet() != Constant.Code.SUCC){
                log.error("addLowerLevelOrgOrStore fail ret:{}|msg:{}",resultDto.getRet(),resultDto.getMsg());
                return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.FAIL);
            }

        }else {
            //参数错误
            return new ResultDto<>(Constant.Code.FAIL,"param is invalid",null);
        }

        //封装结果集
        AdsOfflineOrganize adsOfflineOrganize = new AdsOfflineOrganize();
        adsOfflineOrganize.setParentId(filter.getParentId());
        adsOfflineOrganize.setOrganizeName(filter.getLowerOrgName());
        //创建门店时，组织id和父级id值一样
        if (filter.getOrganizeId() > filter.getParentId()){
            adsOfflineOrganize.setOrganizeId(filter.getOrganizeId());
        }
        return new ResultDto<>(Constant.Code.SUCC,null,adsOfflineOrganize);
    }

    private boolean checkLowerLevelOrgConflict(AdsOfflineOrganizeFilter filter) throws Exception{
        if (filter.getOrganizeId() == 0){
            return false;
        }

        AdsOfflineStoreFilter storeFilter = new AdsOfflineStoreFilter();
        storeFilter.setOrganizeId(filter.getOrganizeId());
        ResultDto<AdsOfflineStore> storeResultDto = adsOfflineStoreService.findOne(storeFilter);
        if (storeResultDto.getRet() != Constant.Code.SUCC){
            throw new RuntimeException();
        }

        //不为空则说明该组织下级是门店
        if (storeResultDto.getData() != null && filter.getLowerOrgType() == Constant.LowerLevelType.ORGANIZE){
            return true;
        }
        return false;
    }

    private void addLowerLevelLead(List<AdsOfflineEmpFilter> leadList,Long organizeId) throws RuntimeException{

        //校验添加机构负责人
        for (AdsOfflineEmpFilter filter : leadList) {

            AdsOfflineEmpFilter empFilter = new AdsOfflineEmpFilter();
            empFilter.setPhone(filter.getPhone());
            //判断是否拥有该成员（没有则添加）
            ResultDto<AdsOfflineEmp> empResultDto = adsOfflineEmpService.findOne(empFilter);
            if (empResultDto.getRet() != Constant.Code.SUCC){
                log.error("addLowerLevelLead findOne fail ret:{}|msg:{}",empResultDto.getRet(),empResultDto.getMsg());
                throw new RuntimeException();
            }

            //没有该成员,添加该成员
            Long empId = null;
            if (empResultDto.getData() == null){
                empFilter.setEmpName(filter.getName());
                ResultDto<Long> addEmpResultDto = adsOfflineEmpService.addEmp(empFilter);
                if (addEmpResultDto.getRet() != Constant.Code.SUCC){
                    log.error("addLowerLevelLead addEmp fail ret:{}|msg:{}",addEmpResultDto.getRet(),addEmpResultDto.getMsg());
                    throw new RuntimeException();
                }

                empId = empFilter.getEmpId();
            }else {
                empId = empResultDto.getData().getEmpId();
            }

            //添加负责人
            AdsOfflineEmpMapFilter empMapFilter = new AdsOfflineEmpMapFilter();
            empMapFilter.setEmpId(empId);
            empMapFilter.setOrganizeId(organizeId);
            ResultDto<Void> addEmpMapresultDto = adsOfflineEmpMapService.addEmpMap(empMapFilter);
            if (addEmpMapresultDto.getRet() != Constant.Code.SUCC){
                log.error("addLowerLevelLead addEmpMap fail param:{}",JSON.toJSONString(empMapFilter));
                throw new RuntimeException();
            }
        }
    }

    @Override
    public ResultDto<AdsOfflineOrganize> importExcel(MultipartFile file, Long organizeId) {
        try{

            if (file == null || StringUtils.isBlank(file.getOriginalFilename()) || organizeId == null){
                log.error("file || fileName || organizeId is empty");
                return new ResultDto<>(Constant.Code.FAIL,"file || fileName || organizeId is empty ");
            }

            //判断该组织架构是否存在
            AdsOfflineOrganizeFilter organizeFilter = new AdsOfflineOrganizeFilter();
            organizeFilter.setDelFlag(0);
            organizeFilter.setOrganizeId(organizeId);
            AdsOfflineOrganize offlineOrganize = adsOfflineOrganizeMapper.findOne(organizeFilter);
            if (offlineOrganize == null){
                return new ResultDto<>(Constant.Code.FAIL,"不存在该机构");
            }
            //获取父级信息
            organizeFilter.setOrganizeId(organizeFilter.getParentId());
            AdsOfflineOrganize parentOrganize = adsOfflineOrganizeMapper.findOne(organizeFilter);
            if (parentOrganize == null){
                return new ResultDto<>(Constant.Code.FAIL,"组织架构层级错误");
            }

            List<AdsOfflineOrganizeExcel> excelList = ExcelUtils.handelExcel(file.getOriginalFilename(), file, AdsOfflineOrganizeExcel.class);
            System.out.println(JSON.toJSONString(excelList));
            for (AdsOfflineOrganizeExcel organizeExcel : excelList) {
                //判断excel中导入该的组织名称是否一致
                String excelLevelOrgName = getExcelOrgLevel(offlineOrganize.getOrganizeLevel(),organizeExcel);
                if (!offlineOrganize.getOrganizeName().equals(excelLevelOrgName)){
                    return new ResultDto<>(Constant.Code.FAIL,offlineOrganize.getOrganizeName()+"该组织名称填写错误");
                }

                //校验父级名称是否一致



            }

        }catch (Exception e){
            log.error("importExcel fail",e);
        }
        return null;
    }

    private String getExcelOrgLevel(Integer organizeLevel,AdsOfflineOrganizeExcel organizeExcel) {

        if (organizeLevel == Constant.LowerLevel.ONE){
            return organizeExcel.getOneLevel();
        }

        if (organizeLevel == Constant.LowerLevel.TWO){
            return organizeExcel.getTwoLevel();
        }

        if (organizeLevel == Constant.LowerLevel.THREE){
            return organizeExcel.getTwoLevel();
        }

        if (organizeLevel == Constant.LowerLevel.FOUR){
            return organizeExcel.getTwoLevel();
        }

        return null;
    }

    @Override
    @Transactional
    public ResultDto<Void> updOrgnize(AdsOfflineOrganizeFilter filter) {
        try{
            //校验入参
            if (filter.getOrganizeId() == null || filter.getStatus() == null ) {
                log.error("updOrgnize organizeId || status is Empty param:{}",JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL, "organizeId || status is Empty");
            }

            //判断该机构是否存在
            AdsOfflineOrganizeFilter offlineOrganizeFilter = new AdsOfflineOrganizeFilter();
            offlineOrganizeFilter.setOrganizeId(filter.getOrganizeId());
            AdsOfflineOrganize offlineOrganize = adsOfflineOrganizeMapper.findOne(offlineOrganizeFilter);
            if (offlineOrganize == null) {
                return new ResultDto<>(Constant.Code.FAIL,"没有该机构");
            }

            //是否成为机构负责人
            if (!CollectionUtils.isEmpty(filter.getLeadList())){

                AdsOfflineEmpMapFilter empMapFilter = new AdsOfflineEmpMapFilter();
                empMapFilter.setLeadList(filter.getLeadList());
                empMapFilter.setAncestorIds(offlineOrganize.getAncestorIds() + COMMA +filter.getOrganizeId());
                //判断是否已经成为负责人（同层级不能重复添加机构负责人）
                ResultDto<Void> leadResult=  adsOfflineEmpMapService.queryLead(empMapFilter);
                if (leadResult.getRet() != Constant.Code.SUCC) {
                    return new ResultDto<>(leadResult.getRet(),leadResult.getMsg());
                }
            }

            //修改机构状态
            AdsOfflineOrganizeFilter organizeFilter = new AdsOfflineOrganizeFilter();
            organizeFilter.setOrganizeId(filter.getOrganizeId());
            organizeFilter.setStatus(filter.getStatus());
            organizeFilter.setOrganizeName(filter.getOrganizeName());
            if (adsOfflineOrganizeMapper.updateOrg(organizeFilter) < 0) {
                log.error("updateOrg fail param:{}",JSON.toJSONString(organizeFilter));
            }

            if (CollectionUtils.isEmpty(filter.getLeadList())){
                return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC);
            }
            //修改人员信息
            for (AdsOfflineEmpFilter adsOfflineEmpFilter : filter.getLeadList()) {

                ResultDto<Void> updateEmpResult = adsOfflineEmpService.updateEmp(adsOfflineEmpFilter);
                if (updateEmpResult .getRet() != Constant.Code.SUCC){
                    log.error("updOrgnize updateEmp fail:{}",JSON.toJSONString(adsOfflineEmpFilter));
                }
            }
            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC);
        }catch (Exception e){
            log.error("updOrgnize fail",e);
            throw new RuntimeException();
        }
    }

    @Override
    public ResultDto<AdsOfflineOrganizeDto> query(AdsOfflineOrganizeFilter filter) {
        try{
            //校验入参
            if (filter.getOrganizeId() == null) {
                log.error("query organizeId is Empty param:{}",JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL, "organizeId is Empty");
            }

            //判断该机构是否存在
            AdsOfflineOrganize offlineOrganize = adsOfflineOrganizeMapper.findOne(filter);
            if (offlineOrganize == null) {
                return new ResultDto<>(Constant.Code.FAIL,"没有该机构");
            }

            AdsOfflineEmpFilter empFilter = new AdsOfflineEmpFilter();
            empFilter.setOrganizeId(empFilter.getOrganizeId());
            ResultDto<List<AdsOfflineEmp>> offlineEmpResult = adsOfflineEmpMapService.findOrgEmp(empFilter);
            if (offlineEmpResult.getRet() != Constant.Code.SUCC){
                log.error("emp query fail ret:{}|msg:{}",offlineEmpResult.getRet(),offlineEmpResult.getMsg());
                return new ResultDto<>(Constant.Code.FAIL,"查询失败");
            }

            //封装结果集
            List<AdsOfflineOrgLead> offlineOrgLeads = new ArrayList<>();
            for (AdsOfflineEmp emp : offlineEmpResult.getData()) {
                AdsOfflineOrgLead lead = new AdsOfflineOrgLead();
                lead.setName(emp.getEmpName());
                lead.setPhone(emp.getPhone());
                lead.setEmpId(emp.getEmpId());

                offlineOrgLeads.add(lead);
            }

            AdsOfflineOrganizeDto offlineOrganizeDto = new AdsOfflineOrganizeDto();
            offlineOrganizeDto.setOrganizeId(offlineOrganize.getOrganizeId());
            offlineOrganizeDto.setOrganizeName(offlineOrganize.getOrganizeName());
            offlineOrganizeDto.setStatus(offlineOrganize.getStatus());
            offlineOrganizeDto.setLeadList(offlineOrgLeads);
            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,offlineOrganizeDto);
        }catch (Exception e){
            log.error("query fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }

    }

    @Override
    public ResultDto<List<AdsOfflineOrganize>> queryOrgTree(AdsOfflineOrganizeFilter filter) {
        try{
            if (checkOrgId(filter)) return new ResultDto<>(Constant.Code.FAIL, "organizeId is Empty");

            //判断该机构是否存在
            AdsOfflineOrganize offlineOrganize = adsOfflineOrganizeMapper.findOne(filter);
            if (offlineOrganize == null || StringUtils.isBlank(offlineOrganize.getAncestorIds())) {
                return new ResultDto<>(Constant.Code.FAIL,"没有该机构");
            }

            filter.setAncestorIds(offlineOrganize.getAncestorIds() + COMMA + offlineOrganize.getOrganizeId());
            filter.setOrganizeId(null);

            List<AdsOfflineOrganize> allOrganize = Lists.newArrayList();
            allOrganize.add(offlineOrganize);
            allOrganize.addAll(adsOfflineOrganizeMapper.findAll(filter));
            return new ResultDto<>(0,"操作成功",allOrganize);
        }catch (Exception e){
            log.error("queryOrgTree fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Object> queryLowerOrg(AdsOfflineOrganizeFilter filter) {
        try{
            if (filter.getPage() == 0 || filter.getPageSize() == 0 || filter.getOrganizeId() == null){
                log.error("queryOrgTree organizeId  || page || pageSize is Empty param:{}",JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL, "organizeId || page || pageSize is Empty");
            }

            //判断该机构是否存在
            AdsOfflineOrganize offlineOrganize = adsOfflineOrganizeMapper.findOne(filter);
            if (offlineOrganize == null || StringUtils.isBlank(offlineOrganize.getAncestorIds())) {
                return new ResultDto<>(Constant.Code.FAIL,"没有该机构");
            }

            //设置分页
            PageHelper.startPage(filter.getPage(),filter.getPageSize());
            filter.setAncestorIds(offlineOrganize.getAncestorIds()+COMMA+filter.getOrganizeId());
            List<AdsOfflineOrganize> offlineOrganizeList = adsOfflineOrganizeMapper.findAll(filter);

            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,new PageInfo<>(offlineOrganizeList));
        }catch (Exception e){
            log.error("queryLowerOrg fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Object> countOrg(AdsOfflineOrganizeFilter filter) {
        try{
            //校验参数
            if (checkOrgId(filter)) return new ResultDto<>(Constant.Code.FAIL, "organizeId is Empty");

            //判断该机构是否存在
            AdsOfflineOrganize offlineOrganize = adsOfflineOrganizeMapper.findOne(filter);
            if (offlineOrganize == null || StringUtils.isBlank(offlineOrganize.getAncestorIds())) {
                return new ResultDto<>(Constant.Code.FAIL,"没有该机构");
            }

            //统计下级数量
            filter.setAncestorIds(offlineOrganize.getAncestorIds() + ","+ filter.getOrganizeId());
            AdsOfflineOrganizeCount organizeCount = new AdsOfflineOrganizeCount();
            Integer orgTotalCount = adsOfflineOrganizeMapper.orgCount(filter);
            organizeCount.setTotalCount(orgTotalCount);
            //下级开通量
            filter.setStatus(Constant.Status.OPEN);
            Integer orgOpenCount = adsOfflineOrganizeMapper.orgCount(filter);
            organizeCount.setOpenCount(orgOpenCount);

            //统计门店数量
            AdsOfflineOrganizeCount storeCount = new AdsOfflineOrganizeCount();
            if (queryStoreCount(filter, storeCount)){
                return new ResultDto<>(Constant.Code.FAIL, "统计失败");
            }

            //统计导购数量
            AdsOfflineOrganizeCount guideCount = new AdsOfflineOrganizeCount();
            if (queryGuideCount(filter,guideCount)) {
                return new ResultDto<>(Constant.Code.FAIL, "统计失败");
            }

            //封装结果集
            Map<String,AdsOfflineOrganizeCount> resultMap = new HashMap<>();
            resultMap.put("lowerLevelCount",organizeCount);
            resultMap.put("storeCount",storeCount);
            resultMap.put("guideCount",guideCount);

            return new ResultDto<>(Constant.Code.SUCC,Constant.ResultMsg.SUCC,resultMap);
        }catch (Exception e){
            log.error("countOrg fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    private boolean queryGuideCount(AdsOfflineOrganizeFilter filter,AdsOfflineOrganizeCount guideCount) {
        //查询导购数量
        AdsOfflineGuideFilter guideFilter = new AdsOfflineGuideFilter();
        guideFilter.setAncestorIds(filter.getAncestorIds());
        ResultDto<Integer> guideTotalCountResult = adsOfflineGuideService.guideCount(guideFilter);
        if (guideTotalCountResult.getRet() != Constant.Code.SUCC){
            log.error("guide totalCount fail:{}", JSON.toJSONString(guideFilter));
            return true;
        }
        guideCount.setTotalCount(guideTotalCountResult.getData());

        //查询开通导购数量
        guideFilter.setStatus(Constant.Status.OPEN);
        ResultDto<Integer> guideOpneCountResult = adsOfflineGuideService.guideCount(guideFilter);
        if (guideOpneCountResult.getRet() != Constant.Code.SUCC){
            log.error("guide totalCount fail:{}", JSON.toJSONString(guideFilter));
            return true;
        }
        guideCount.setOpenCount(guideOpneCountResult.getData());
        return false;
    }

    private boolean queryStoreCount(AdsOfflineOrganizeFilter filter, AdsOfflineOrganizeCount storeCount) {
        //查询门店数量
        AdsOfflineStoreFilter storeFilter = new AdsOfflineStoreFilter();
        storeFilter.setAncestorIds(filter.getAncestorIds());
        ResultDto<Integer> storeTotalCount = adsOfflineStoreService.storeCount(storeFilter);
        if (storeTotalCount.getRet() != Constant.Code.SUCC){
            log.error("store totalCount fail:{}", JSON.toJSONString(storeFilter));
            return true;
        }
        storeCount.setTotalCount(storeTotalCount.getData());
        //查询门店开通数量
        storeFilter.setStatus(Constant.Status.OPEN);
        ResultDto<Integer> storeOpenCount = adsOfflineStoreService.storeCount(storeFilter);
        if (storeOpenCount.getRet() != Constant.Code.SUCC){
            log.error("store openCount fail:{}",JSON.toJSONString(storeFilter));
            return true;
        }
        storeCount.setOpenCount(storeOpenCount.getData());
        return false;
    }

    private boolean checkOrgId(AdsOfflineOrganizeFilter filter) {
        if (filter.getOrganizeId() == null) {
            log.error("queryOrgTree organizeId is Empty");
            return true;
        }
        return false;
    }

    @Override
    public ResultDto<AdsOfflineOrganize> queryOrg(AdsOfflineOrganizeFilter filter) {
        try{
            return new ResultDto<>(Constant.Code.SUCC,null,adsOfflineOrganizeMapper.findOne(filter));
        }catch (Exception e){
            log.error("queryOrg fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<List<AdsOfflineOrganize>> queryHigherLevel(AdsOfflineOrganizeFilter filter) {
        try{

            return  new ResultDto<>(Constant.Code.SUCC,null,adsOfflineOrganizeMapper.findHigherLevel(filter));
        }catch (Exception e){
            log.error("queryOrg fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Void> batchUpdOrgStatus(AdsOfflineOrganizeFilter filter) {
        try{
            if (CollectionUtils.isEmpty(filter.getOrganizeIdList()) || filter.getStatus() == null){
                log.error("organizeIdList || status is empty:{}",JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL,"organizeIdList || status is empty");
            }

            for (Long organizeId: filter.getOrganizeIdList()) {
                AdsOfflineOrganizeFilter organizeFilter = new AdsOfflineOrganizeFilter();
                organizeFilter.setOrganizeId(organizeId);
                organizeFilter.setStatus(filter.getStatus());
                if (adsOfflineOrganizeMapper.updateOrg(organizeFilter) < 0){
                     log.error("batchUpdOrgStatus fail param:{}",JSON.toJSONString(organizeFilter));
                }
            }

            return new ResultDto<>();
        }catch (Exception e){
            log.error("batchUpdOrgStatus fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }

    @Override
    public ResultDto<Void> delOrg(AdsOfflineOrganizeFilter filter) {
        try{
            if (CollectionUtils.isEmpty(filter.getOrganizeIdList())){
                log.error("organizeIdList || status is empty:{}",JSON.toJSONString(filter));
                return new ResultDto<>(Constant.Code.FAIL,"");
            }

            for (Long organizeId: filter.getOrganizeIdList()) {
                AdsOfflineOrganizeFilter organizeFilter = new AdsOfflineOrganizeFilter();
                organizeFilter.setOrganizeId(organizeId);
                organizeFilter.setDelFlag(Constant.DelFlag.INVALID);
                if (adsOfflineOrganizeMapper.updateOrg(organizeFilter) < 0){
                    log.error("batchUpdOrgStatus fail param:{}",JSON.toJSONString(organizeFilter));
                }
            }

            return new ResultDto<>();
        }catch (Exception e){
            log.error("delOrg fail",e);
            return new ResultDto<>(Constant.Code.FAIL,Constant.ResultMsg.SYSTEM_ERROR);
        }
    }
}
