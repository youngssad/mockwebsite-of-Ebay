package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.User;
import com.example.exception.MMallException;
import com.example.form.UserLoginForm;
import com.example.form.UserRegisterForm;
import com.example.mapper.UserMapper;
import com.example.result.ResponseEnum;
import com.example.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.utils.MD5Util;
import com.example.utils.RegexValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    //userRegister
    @Override
    public User register(UserRegisterForm userRegisterForm) {
        //check if the username is already registered
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name", userRegisterForm.getLoginName());
        User one = this.userMapper.selectOne(queryWrapper);
        if(one != null){
            log.info("username already exists");
            throw new MMallException(ResponseEnum.USERNAME_EXISTS);
        }
        //check email format
        if(!RegexValidateUtil.checkEmail(userRegisterForm.getEmail())){
            log.info("user email format error");
            throw new MMallException(ResponseEnum.EMAIL_ERROR);
        }
        //check mobile format
        if(!RegexValidateUtil.checkMobile(userRegisterForm.getMobile())){
            log.info("user mobile format error");
            throw new MMallException(ResponseEnum.MOBILE_ERROR);
        }
        //save data
        User user = new User();
        BeanUtils.copyProperties(userRegisterForm, user);
        user.setPassword(MD5Util.getSaltMD5(user.getPassword()));
        int insert = this.userMapper.insert(user);
        if(insert != 1){
            log.info("user register failed");
            throw new MMallException(ResponseEnum.USER_REGISTER_ERROR);
        }
        return user;
    }

    //userLogin
    @Override
    public User login(UserLoginForm userLoginForm) {
        //check if user exists
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name", userLoginForm.getLoginName());
        User user = this.userMapper.selectOne(queryWrapper);
        if(user == null){
            log.info("user not exists");
            throw new MMallException(ResponseEnum.USERNAME_NOT_EXISTS);
        }
        //check password
        boolean saltverifyMD5 = MD5Util.getSaltverifyMD5(userLoginForm.getPassword(), user.getPassword());
        if(!saltverifyMD5){
            log.info("wrong password");
            throw new MMallException(ResponseEnum.PASSWORD_ERROR);
        }
        return user;
    }
}
