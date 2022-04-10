package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.OrderDetail;
import com.example.entity.Orders;
import com.example.entity.Product;
import com.example.mapper.OrderDetailMapper;
import com.example.mapper.OrdersMapper;
import com.example.mapper.ProductMapper;
import com.example.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vo.OrderDetailVO;
import com.example.vo.OrdersVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<OrdersVO> findAllByUserId(Integer id) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        List<Orders> ordersList = this.ordersMapper.selectList(queryWrapper);
        List<OrdersVO> ordersVOList = new ArrayList<>();
        for (Orders orders : ordersList) {
            OrdersVO ordersVO = new OrdersVO();
            BeanUtils.copyProperties(orders, ordersVO);
            QueryWrapper<OrderDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("order_id", orders.getId());
            List<OrderDetail> orderDetailList = this.orderDetailMapper.selectList(queryWrapper1);
            List<OrderDetailVO> orderDetailVOList = new ArrayList<>();
            for (OrderDetail orderDetail : orderDetailList) {
                OrderDetailVO orderDetailVO = new OrderDetailVO();
                BeanUtils.copyProperties(orderDetail, orderDetailVO);
                Product product = this.productMapper.selectById(orderDetail.getProductId());
                BeanUtils.copyProperties(product, orderDetailVO);
                orderDetailVOList.add(orderDetailVO);
            }
            ordersVO.setOrderDetailList(orderDetailVOList);
            ordersVOList.add(ordersVO);
        }
        return ordersVOList;
    }
}
