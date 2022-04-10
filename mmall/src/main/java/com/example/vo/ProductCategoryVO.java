package com.example.vo;

import com.example.entity.Product;
import com.example.entity.ProductCategory;
import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryVO {
    private Integer id;
    private String name;
    private Integer parentId;
    private List<ProductCategoryVO> children;
    private List<Product> productList;

    public ProductCategoryVO(ProductCategory productCategory) {
        this.id = productCategory.getId();
        this.name = productCategory.getName();
        this.parentId = productCategory.getParentId();
    }
}
