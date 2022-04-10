package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.*;
import com.example.exception.MMallException;
import com.example.mapper.*;
import com.example.result.ResponseEnum;
import com.example.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vo.CartVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
@Slf4j
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    @Transactional
    public Boolean add(Cart cart) {
        //add to cart
        int insert = this.cartMapper.insert(cart);
        if(insert != 1){
            throw new MMallException(ResponseEnum.CART_ADD_ERROR);
        }
        //product stock adjust
        Integer stock = this.productMapper.getStockById(cart.getProductId());
        if(stock == null){
            log.info("add to cart error, product not exist");
            throw new MMallException(ResponseEnum.PRODUCT_NOT_EXISTS);
        }
        if(stock == 0){
            log.info("add to cart error, product stock is not enough");
            throw new MMallException(ResponseEnum.PRODUCT_STOCK_ERROR);
        }
        Integer newStock = stock - cart.getQuantity();
        if(newStock < 0){
            log.info("add to cart error, product stock is not enough");
            throw new MMallException(ResponseEnum.PRODUCT_STOCK_ERROR);
        }
        this.productMapper.updateStockById(cart.getProductId(), newStock);
        return true;
    }

    @Override
    public List<CartVO> findVOListByUserId(Integer userId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Cart> cartList = this.cartMapper.selectList(queryWrapper);
        List<CartVO> cartVOList = new ArrayList<>();
        for (Cart cart : cartList) {
            Product product = this.productMapper.selectById(cart.getProductId());
            CartVO cartVO = new CartVO();
            BeanUtils.copyProperties(product, cartVO);
            BeanUtils.copyProperties(cart, cartVO);
            cartVOList.add(cartVO);
        }
        return cartVOList;
    }

    @Override
    @Transactional
    public Boolean update(Integer id, Integer quantity, Float cost) {
        //update cart
        Cart cart = this.cartMapper.selectById(id);
        Integer oldQuantity = cart.getQuantity();
        if(quantity.equals(oldQuantity)){
            log.info("update cart error, cart parameter error");
            throw new MMallException(ResponseEnum.CART_UPDATE_PARAMETER_ERROR);
        }
        //查询商品库存
        Integer stock = this.productMapper.getStockById(cart.getProductId());
        Integer newStock = stock - (quantity - oldQuantity);
        if(newStock < 0){
            log.info("update cart error, product stock error");
            throw new MMallException(ResponseEnum.PRODUCT_STOCK_ERROR);
        }
        Integer integer = this.productMapper.updateStockById(cart.getProductId(), newStock);
        if(integer != 1){
            log.info("update cart error, product stock error");
            throw new MMallException(ResponseEnum.CART_UPDATE_STOCK_ERROR);
        }
        //更新数据
        int update = this.cartMapper.update(id, quantity, cost);
        if(update != 1){
            log.info("update cart error, cart update error");
            throw new MMallException(ResponseEnum.CART_UPDATE_ERROR);
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean delete(Integer id) {
        //upate product stock
        Cart cart = this.cartMapper.selectById(id);
        Integer stock = this.productMapper.getStockById(cart.getProductId());
        Integer newStock = stock + cart.getQuantity();
        Integer integer = this.productMapper.updateStockById(cart.getProductId(), newStock);
        if(integer != 1){
            log.info("delete cart error, product stock error");
            throw new MMallException(ResponseEnum.CART_UPDATE_STOCK_ERROR);
        }
        //删除购物车数据
        int i = this.cartMapper.deleteById(id);
        if(i != 1){
            log.info("delete cart error, cart delete error");
            throw new MMallException(ResponseEnum.CART_REMOVE_ERROR);
        }
        return true;
    }

    @Override
    @Transactional
    public Orders commit(String userAddress, String address, String remark, User user) {
        //update address
        if(!userAddress.equals("newAddress")){
            address = userAddress;
        }else{
            int i = this.userAddressMapper.setDefault();
            if(i == 0){
                log.info("confirm order error, update address error");
                throw new MMallException(ResponseEnum.USER_ADDRESS_SET_DEFAULT_ERROR);
            }
            //update new address in database
            UserAddress userAddress1 = new UserAddress();
            userAddress1.setIsdefault(1);
            userAddress1.setUserId(user.getId());
            userAddress1.setRemark(remark);
            userAddress1.setAddress(address);
            int insert = this.userAddressMapper.insert(userAddress1);
            if(insert == 0){
                log.info("confirm order error, update address error");
                throw new MMallException(ResponseEnum.USER_ADDRESS_ADD_ERROR);
            }
        }
        //create order
        Orders orders = new Orders();
        orders.setUserId(user.getId());
        orders.setLoginName(user.getLoginName());
        orders.setUserAddress(address);
        orders.setCost(this.cartMapper.getCostByUserId(user.getId()));
        String seriaNumber = null;
        try {
            StringBuffer result = new StringBuffer();
            for(int i=0;i<32;i++) {
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            seriaNumber =  result.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        orders.setSerialnumber(seriaNumber);
        int insert = this.ordersMapper.insert(orders);
        if(insert != 1){
            log.info("confirm order error, order create error");
            throw new MMallException(ResponseEnum.ORDERS_CREATE_ERROR);
        }
        //create order detail
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        List<Cart> carts = this.cartMapper.selectList(queryWrapper);
        for (Cart cart : carts) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            int insert1 = this.orderDetailMapper.insert(orderDetail);
            if(insert1 == 0){
                log.info("create order detail error");
                throw new MMallException(ResponseEnum.ORDER_DETAIL_CREATE_ERROR);
            }
        }
        //empty cart
        QueryWrapper<Cart> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("user_id", user.getId());
        int delete = this.cartMapper.delete(queryWrapper1);
        if(delete == 0){
            log.info("create order error, cart delete error");
            throw new MMallException(ResponseEnum.CART_REMOVE_ERROR);
        }
        return orders;
    }
}
