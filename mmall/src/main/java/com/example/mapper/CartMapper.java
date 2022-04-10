package com.example.mapper;

import com.example.entity.Cart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


public interface CartMapper extends BaseMapper<Cart> {
    public int update(Integer id,Integer quantity,Float cost);
    public Float getCostByUserId(Integer id);
}
