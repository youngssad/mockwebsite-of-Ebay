package com.example.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UserLoginForm {
    @NotEmpty(message = "login.username.empty")
    private String loginName;
    @NotEmpty(message = "login.password.empty")
    private String password;
}
