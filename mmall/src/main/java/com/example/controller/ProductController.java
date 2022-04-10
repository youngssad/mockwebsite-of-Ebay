package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.exception.MMallException;
import com.example.result.ResponseEnum;
import com.example.service.CartService;
import com.example.service.ProductCategoryService;
import com.example.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;


@Controller
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private CartService cartService;

    /**
     * @param type
     * @param productCategoryId
     * @param session
     * @return
     */
    @GetMapping("/list/{type}/{id}")
    public ModelAndView list(
            @PathVariable("type") Integer type,
            @PathVariable("id") Integer productCategoryId,
            HttpSession session
    ){
        if(type == null || productCategoryId == null){
            log.info("product param is null");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("productList");
        modelAndView.addObject("productList", this.productService.findAllByTypeAndProductCategoryId(type, productCategoryId));
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
        //product category list
        modelAndView.addObject("list", this.productCategoryService.buildProductCategoryMenu());
        return modelAndView;
    }

    /**
     * search product
     * @param keyWord
     * @param session
     * @return
     */
    @PostMapping("/search")
    public ModelAndView search(String keyWord,HttpSession session){
        if(keyWord == null){
            log.info("search product param is null");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("productList");
        //fuzzy search data
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", keyWord);
        modelAndView.addObject("productList",this.productService.list(queryWrapper));
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
        //product category list
        modelAndView.addObject("list", this.productCategoryService.buildProductCategoryMenu());
        return modelAndView;
    }

    /**
     * product detail
     * @param id
     * @param session
     * @return
     */
    @GetMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable("id") Integer id,HttpSession session){
        if(id == null){
            log.info("product param is null");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("productDetail");
        //check if user is login
        User user = (User) session.getAttribute("user");
        if(user == null){
            //not login
            modelAndView.addObject("cartList", new ArrayList<>());
        }else{
            //  already login
            //check user cart info
            modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
        }
        //product category list
        modelAndView.addObject("list", this.productCategoryService.buildProductCategoryMenu());
        //product detail
        modelAndView.addObject("product", this.productService.getById(id));
        return modelAndView;
    }

}

