package com.example.mapper;

import com.example.entity.UserAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UserAddressMapper extends BaseMapper<UserAddress> {
    public int setDefault();
}
