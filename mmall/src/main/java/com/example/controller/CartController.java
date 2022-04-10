package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Cart;
import com.example.entity.Orders;
import com.example.entity.User;
import com.example.exception.MMallException;
import com.example.result.ResponseEnum;
import com.example.service.CartService;
import com.example.service.UserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/cart")
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserAddressService userAddressService;

    //add cart
    @GetMapping("/add/{productId}/{price}/{quantity}")
    public String add(
            @PathVariable("productId") Integer productId,
            @PathVariable("price") Float price,
            @PathVariable("quantity") Integer quantity,
            HttpSession session
    ){
        if(productId == null || price == null || quantity == null){
            log.info("cart add parameter is null");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        //check if user is login
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("user is not login");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        Cart cart = new Cart();
        cart.setUserId(user.getId());
        cart.setProductId(productId);
        cart.setQuantity(quantity);
        cart.setCost(price * quantity);
        Boolean add = this.cartService.add(cart);
        if(!add){
            log.info("add cart fail");
            throw new MMallException(ResponseEnum.CART_ADD_ERROR);
        }
        return "redirect:/cart/get";
    }

    //get cart
    @GetMapping("/get")
    public ModelAndView get(HttpSession session){
        //check if user is login
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("user is not login");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settlement1");
        modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
        return modelAndView;
    }

    //update cart
    @PostMapping("/update/{id}/{quantity}/{cost}")
    @ResponseBody
    public String update(
            @PathVariable("id") Integer id,
            @PathVariable("quantity") Integer quantity,
            @PathVariable("cost") Float cost,
            HttpSession session
    ){
        if(id == null || quantity == null || cost == null){
            log.info("cart update parameter is null");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        //check if user is login
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("user is not login");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        if(this.cartService.update(id, quantity, cost)) return "success";
        return "fail";
    }

    //delete cart
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id,HttpSession session){
        if(id == null){
            log.info("update cart parameter is null");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        //check if user is login
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("user is not login");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        Boolean delete = this.cartService.delete(id);
        if(delete) return "redirect:/cart/get";
        return null;
    }

    //confirm order
    @GetMapping("/confirm")
    public ModelAndView confirm(HttpSession session){
        //check if user is login
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("user is not login");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settlement2");
        modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", user.getId());
        modelAndView.addObject("addressList", this.userAddressService.list(queryWrapper));
        return modelAndView;
    }

    //submit order
    @PostMapping("/commit")
    public ModelAndView commit(
            String userAddress,
            String address,
            String remark,
            HttpSession session){
        if(userAddress == null || address == null || remark == null){
            log.info("order commit parameter is null");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        //check if user is login
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("user is not login");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settlement3");
        Orders orders = this.cartService.commit(userAddress, address, remark, user);
        if(orders != null){
            modelAndView.addObject("orders", orders);
            modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
            return modelAndView;
        }
        return null;
    }
}

