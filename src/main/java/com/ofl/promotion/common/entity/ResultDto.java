package com.ofl.promotion.common.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResultDto<T> {

    private int ret;

    private T data;

    private String msg;

    public ResultDto() {
    }

    public ResultDto(int ret, String msg) {
        this.ret = ret;
        this.msg = msg;
    }

    public ResultDto(int ret, String msg, T data) {
        this.ret = ret;
        this.msg = msg;
        this.data = data;
    }
}
