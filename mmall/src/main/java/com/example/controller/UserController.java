package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.User;
import com.example.entity.UserAddress;
import com.example.exception.MMallException;
import com.example.form.UserLoginForm;
import com.example.form.UserRegisterForm;
import com.example.result.ResponseEnum;
import com.example.service.CartService;
import com.example.service.OrdersService;
import com.example.service.UserAddressService;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserAddressService userAddressService;

    /**
     * user register
     * @param userRegisterForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid UserRegisterForm userRegisterForm, BindingResult bindingResult){
        //not null check
        if(bindingResult.hasErrors()){
            log.info("user register info is null");
            throw new MMallException(ResponseEnum.USER_INFO_NULL);
        }
        User register = this.userService.register(userRegisterForm);
        if(register == null){
            log.info("user register fail to add");
            throw new MMallException(ResponseEnum.USER_REGISTER_ERROR);
        }
        return "redirect:/login";
    }

    /**
     * user login
     * @return
     */
    @PostMapping("/login")
    public String login(@Valid UserLoginForm userLoginForm, BindingResult bindingResult, HttpSession session){
        //not null check
        if(bindingResult.hasErrors()){
            log.info("user login info is null");
            throw new MMallException(ResponseEnum.USER_INFO_NULL);
        }
        User login = this.userService.login(userLoginForm);
        session.setAttribute("user",login);
        return "redirect:/productCategory/main";
    }

    /**
     * return user order list
     * @return
     */
    @GetMapping("/orderList")
    public ModelAndView ordersList(HttpSession session){
        //check user login
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("update cart user is null");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("orderList");
        modelAndView.addObject("orderList", this.ordersService.findAllByUserId(user.getId()));
        modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
        return modelAndView;
    }

    /**
     * return user address list
     * @return
     */
    @GetMapping("/addressList")
    public ModelAndView addressList(HttpSession session){
        //check if user login
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("update cart user is null");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userAddressList");
        QueryWrapper<UserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        modelAndView.addObject("addressList", this.userAddressService.list(queryWrapper));
        modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
        return modelAndView;
    }
}

