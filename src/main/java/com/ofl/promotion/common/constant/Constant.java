package com.ofl.promotion.common.constant;

/**
 * @Author Mr.quan
 * @Date 2021/8/21 13:10
 */
public class Constant {

    /**
     * 状态码
     */
    public interface Code {
       int SUCC = 0;
       int FAIL = -1;
    }

    /**
     * 下级机构类型
     */
    public interface LowerLevelType {
        int ORGANIZE = 1;//组织机构
        int STORE = 2;//门店
    }

    /**
     * 返回信息
     */
    public interface ResultMsg {
        String FAIL = "操作失败";
        String SUCC = "操作成功";
        String SYSTEM_ERROR = "服务器开小差了";
        String LOWER_LEVEL_ORG_TYPE_CONFLICT = "下级机构类型冲突";
        String TOKEN_INVALID = "登录态失效，请重新登录";
        String LOGIN_FAIL = "登录失败";
        String PARAM_INVALID_FAIL = "参数无效";
    }

    /**
     * 下级机构类型
     */
    public interface LowerLevel{
        int ONE = 1;//一级
        int TWO = 2;//二级
        int THREE = 3;//三级
        int FOUR = 4;//四级
    }

    /**
     * 机构，门店，导购状态
     */
    public interface Status{
        int OPEN = 1;//启用
        int STOP = 2;//停用
    }

    /**
     * 机构，门店，导购删除状态
     */
    public interface DelFlag{
        int VALID = 0;//有效状态
        int INVALID = 1;//无效状态
    }

    /**
     * 开通业务
     */
    public interface Business{
        int DOUYIN = 1;
    }

    /*
    * 登录类型
    */
    public interface LoginType{
        int APPLET = 1;//小程序
        int PC = 1;//PC端

    }
}
