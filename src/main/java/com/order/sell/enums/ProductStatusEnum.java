package com.order.sell.enums;

import lombok.Getter;

/**
 * 商品状态
 * @program: sell
 * @description:
 * @author: Mr.Wang
 * @create: 2019-10 12:31
 */
@Getter
public enum ProductStatusEnum {
    UP(0, "在架"),
    DOWN(1, "下架")
    ;
    private Integer code;
    private String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
