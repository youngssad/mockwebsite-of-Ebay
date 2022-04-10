package com.example.mapper;

import com.example.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


public interface ProductMapper extends BaseMapper<Product> {
    public Integer updateStockById(Integer id,Integer stock);
    public Integer getStockById(Integer id);
}
