package com.example.service;

import com.example.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Orders;
import com.example.entity.User;
import com.example.vo.CartVO;

import java.util.List;

public interface CartService extends IService<Cart> {
    public Boolean add(Cart cart);
    public List<CartVO> findVOListByUserId(Integer userId);
    public Boolean update(Integer id,Integer quantity,Float cost);
    public Boolean delete(Integer id);
    public Orders commit(String userAddress, String address, String remark, User user);
}
