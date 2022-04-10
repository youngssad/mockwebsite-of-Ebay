package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
  @EqualsAndHashCode(callSuper = false)
    public class Orders implements Serializable {

    private static final long serialVersionUID=1L;


        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;


      private Integer userId;

      private String loginName;


      private String userAddress;


      private Float cost;


      private String serialnumber;


        @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;


        @TableField(fill = FieldFill.INSERT_UPDATE)
      private LocalDateTime updateTime;


}
