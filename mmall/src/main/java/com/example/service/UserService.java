package com.example.service;

import com.example.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.form.UserLoginForm;
import com.example.form.UserRegisterForm;


public interface UserService extends IService<User> {
    public User register(UserRegisterForm userRegisterForm);
    public User login(UserLoginForm userLoginForm);
}
