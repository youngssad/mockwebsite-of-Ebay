package com.example.service;

import com.example.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vo.OrdersVO;

import java.util.List;


public interface OrdersService extends IService<Orders> {
    public List<OrdersVO> findAllByUserId(Integer id);
}
