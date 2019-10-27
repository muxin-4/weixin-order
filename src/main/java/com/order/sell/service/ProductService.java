package com.order.sell.service;

import com.order.sell.dataobject.ProductInfo;

import java.util.List;

import com.order.sell.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 商品
 */
public interface ProductService {


    ProductInfo findOne(String productId);

    /**
     * 查询所在架商品列表
     * @return
     */
    List<ProductInfo> findUpAll();

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    // 加库存
    void inCreaseStock(List<CartDTO> cartDTOList);

    // 减库存
    void decreaseStock(List<CartDTO> cartDTOList);
}
