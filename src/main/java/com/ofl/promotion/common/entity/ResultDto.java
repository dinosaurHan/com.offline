package com.ofl.promotion.common.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResultDto<T> {

    private int ret;

    private T data;

    private String msg;
}
