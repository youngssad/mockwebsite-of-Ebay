package com.example.service;

import com.example.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ProductService extends IService<Product> {
    public List<Product> findAllByTypeAndProductCategoryId(Integer type,Integer id);
}
