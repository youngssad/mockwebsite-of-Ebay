package com.example.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class UserRegisterForm {
    @NotEmpty(message = "login can not be empty")
    private String loginName;
    @NotEmpty(message = "userName can not be empty")
    private String userName;
    @NotEmpty(message = "password can not be empty")
    private String password;
    @NotNull(message = "gender can not be empty")
    private Integer gender;
    @NotEmpty(message = "email can not be empty")
    private String email;
    @NotEmpty(message = "mobile number can not be empty")
    private String mobile;
}
