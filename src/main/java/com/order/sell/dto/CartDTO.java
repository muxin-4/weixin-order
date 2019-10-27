package com.order.sell.dto;

import lombok.Data;

/**
 * @program: sell
 * @description:
 * @author: Mr.Wang
 * @create: 2019-27 17:40
 */
@Data
public class CartDTO {

    /** 商品Id. */
    private String productId;

    /** 数量. */
    private Integer productQuantiry;

    public CartDTO(String productId, Integer productQuantiry) {
        this.productId = productId;
        this.productQuantiry = productQuantiry;
    }
}
