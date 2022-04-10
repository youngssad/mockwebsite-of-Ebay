package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
  @EqualsAndHashCode(callSuper = false)
    public class Product implements Serializable {

    private static final long serialVersionUID=1L;

        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      private String name;

      private String description;

      private Float price;

      private Integer stock;

      private Integer categoryleveloneId;


      private Integer categoryleveltwoId;


      private Integer categorylevelthreeId;


      private String fileName;


}
