package com.order.sell.dto;

import com.order.sell.dataobject.OrderDetail;
import com.order.sell.enums.OrderStatusEnum;
import com.order.sell.enums.PayStatusEnum;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: sell
 * @description:
 * @author: Mr.Wang
 * @create: 2019-27 10:44
 */
@Data
public class OrderDTO {
    /** 订单id. */
    private String orderId;

    /** 买家名字. */
    private String buyerName;

    /** 买家手机号. */
    private String buyerPhone;

    /** 买家地址. */
    private String buyerAddress;

    /** 买家微信Openid. */
    private String buyerOpenid;

    /** 订单金额 */
    private BigDecimal orderAmount;

    private Integer orderStatus;

    /** 支付状态，默认为 0 未支付 */
    private Integer payStatus;

    /** 创建时间. */
    private Date createTime;

    /** 修改时间. */
    private Date updateTime;

    List<OrderDetail> orderDetailList;
}
