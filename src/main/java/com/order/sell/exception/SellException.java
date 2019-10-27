package com.order.sell.exception;

import com.order.sell.enums.ResultEnum;

/**
 * @program: sell
 * @description:
 * @author: Mr.Wang
 * @create: 2019-27 12:01
 */
public class SellException extends RuntimeException {
    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }
}
