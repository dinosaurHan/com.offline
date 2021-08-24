package com.ofl.promotion.manage.store.entity.filter;

import com.ofl.promotion.manage.store.entity.AdsOfflineStore;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Mr.quan
 * @Date 2021/8/22 12:00
 */
@Data
public class AdsOfflineStoreFilter extends AdsOfflineStore implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ancestorIds;
}
