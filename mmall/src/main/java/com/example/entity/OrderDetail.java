package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
  @EqualsAndHashCode(callSuper = false)
    public class OrderDetail implements Serializable {

    private static final long serialVersionUID=1L;

        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;


      private Integer orderId;


      private Integer productId;


      private Integer quantity;

      private Float cost;


}
