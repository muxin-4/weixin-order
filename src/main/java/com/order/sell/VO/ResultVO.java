package com.order.sell.VO;

import lombok.Data;

/**
 * http 请求返回的最外层对象
 *
 * @program: sell
 * @description:
 * @author: Mr.Wang
 * @create: 2019-19 19:52
 */
@Data
public class ResultVO<T> {

    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 具体内容. */
    private T data;
}
