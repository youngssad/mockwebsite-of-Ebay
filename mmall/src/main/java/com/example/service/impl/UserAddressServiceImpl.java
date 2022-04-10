package com.example.service.impl;

import com.example.entity.UserAddress;
import com.example.mapper.UserAddressMapper;
import com.example.service.UserAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

}
