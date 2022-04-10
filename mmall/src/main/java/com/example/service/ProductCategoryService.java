package com.example.service;

import com.example.entity.ProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vo.ProductCategoryVO;

import java.util.List;

public interface ProductCategoryService extends IService<ProductCategory> {
    public List<ProductCategoryVO> buildProductCategoryMenu();
    public List<ProductCategoryVO> findAllProductByCategoryLevelOne();
}
