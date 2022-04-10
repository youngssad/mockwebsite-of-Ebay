package com.example.controller;


import com.example.entity.User;
import com.example.service.CartService;
import com.example.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;


@Controller
@RequestMapping("/productCategory")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private CartService cartService;


    //redirect to main page
    @GetMapping("/main")
    public ModelAndView main(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("main");
        //encapulate product category list
        modelAndView.addObject("list", this.productCategoryService.buildProductCategoryMenu());
        //encapulate product category level one list
        modelAndView.addObject("levelOneProductList",this.productCategoryService.findAllProductByCategoryLevelOne());
        //check if user is login
        User user = (User) session.getAttribute("user");
        if(user == null){
            //not login
            modelAndView.addObject("cartList", new ArrayList<>());
        }else{
            //already login
            //check user cart info
            modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
        }
        return modelAndView;
    }

}

