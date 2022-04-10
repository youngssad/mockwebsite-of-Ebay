package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Product;
import com.example.entity.ProductCategory;
import com.example.mapper.ProductCategoryMapper;
import com.example.mapper.ProductMapper;
import com.example.service.ProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vo.ProductCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Autowired
    private ProductMapper productMapper;

    //productCategoryVOList
    @Override
    public List<ProductCategoryVO> buildProductCategoryMenu() {
        //1、search all productCategory
        List<ProductCategory> productCategoryList = this.productCategoryMapper.selectList(null);
        //2、transfer to ProductCategoryVO
        List<ProductCategoryVO> productCategoryVOList = productCategoryList.stream().map(ProductCategoryVO::new).collect(Collectors.toList());
        //3、build menu
        List<ProductCategoryVO> levelOneList = buildMenu(productCategoryVOList);
        return levelOneList;
    }

    @Override
    public List<ProductCategoryVO> findAllProductByCategoryLevelOne() {
        QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", 1);
        List<ProductCategory> productCategoryList = this.productCategoryMapper.selectList(queryWrapper);
        List<ProductCategoryVO> productCategoryVOList = productCategoryList.stream().map(ProductCategoryVO::new).collect(Collectors.toList());
        getLevelOneProduct(productCategoryVOList);
        return productCategoryVOList;
    }

    //productcategory level1
    public void getLevelOneProduct(List<ProductCategoryVO> list){
        for (ProductCategoryVO vo : list) {
            QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("categorylevelone_id", vo.getId());
            List<Product> productList = this.productMapper.selectList(queryWrapper);
            vo.setProductList(productList);
        }
    }

    //Menu
    public List<ProductCategoryVO> buildMenu(List<ProductCategoryVO> list){
        //找到一级菜单
        List<ProductCategoryVO> levelOneList = list.stream().filter(c -> c.getParentId() == 0).collect(Collectors.toList());
        for (ProductCategoryVO vo : levelOneList) {
            recursion(list,vo);
        }
        return levelOneList;
    }

    //recursion
    public void recursion(List<ProductCategoryVO> list,ProductCategoryVO vo){
        //find children
        List<ProductCategoryVO> children = getChildren(list, vo);
        vo.setChildren(children);
        //recursion
        if(children.size() > 0){
            for (ProductCategoryVO child : children) {
                recursion(list,child);
            }
        }
    }

    //find children list
    public List<ProductCategoryVO> getChildren(List<ProductCategoryVO> list,ProductCategoryVO vo){
        List<ProductCategoryVO> children = new ArrayList<>();
        Iterator<ProductCategoryVO> iterator = list.iterator();
        while (iterator.hasNext()) {
            ProductCategoryVO next = iterator.next();
            if(next.getParentId().equals(vo.getId())){
                children.add(next);
            }
        }
        return children;
    }
}
