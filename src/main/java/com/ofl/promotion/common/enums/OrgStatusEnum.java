package com.ofl.promotion.common.enums;

/**
 * @Author Mr.quan
 * @Date 2021/8/23 21:10
 */
public enum OrgStatusEnum {


    ONE_LEVEL(1,"启用"),
    TWO_LEVEL(2,"停用");

    private int status;
    private String statusName;

    public static String getStatusName(int status) {
        for (OrgStatusEnum statusEnum : OrgStatusEnum.values()) {
            if (statusEnum.getStatus() == status) {
                return statusEnum.getStatusName();
            }
        }
        return null;
    }

    OrgStatusEnum(int status, String statusName) {
        this.status = status;
        this.statusName = statusName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
