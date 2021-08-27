package com.ofl.promotion.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Mr.quan
 * @Date 2021/8/25 0:10
 */
public class DateUtils {

    //时间戳转string  timeStamp：秒级
    public static String conversionTime(Long timeStamp) {
        //yyyy-MM-dd HH:mm:ss 转换的时间格式  可以自定义
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换
        return sdf.format(new Date(timeStamp*1000));
    }

}
