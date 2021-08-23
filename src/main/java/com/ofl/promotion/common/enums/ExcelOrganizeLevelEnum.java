package com.ofl.promotion.common.enums;

/**
 * @Author Mr.quan
 * @Date 2021/8/23 21:10
 */
public enum ExcelOrganizeLevelEnum {


    ONE_LEVEL(1,"oneLevel"),
    TWO_LEVEL(2,"twoLevel"),
    THREE_LEVEL(3,"threeLevel");

    private int level;
    private String levelName;

    public static String getLevelName(int levelPram) {
        for (ExcelOrganizeLevelEnum levelEnum : ExcelOrganizeLevelEnum.values()) {
            if (levelEnum.getLevel() == levelPram) {
                return levelEnum.getLevelName();
            }
        }
        return null;
    }

    ExcelOrganizeLevelEnum(int level, String levelName) {
        this.level = level;
        this.levelName = levelName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
