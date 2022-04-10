package com.example.result;

public enum ResponseEnum {
    USER_INFO_NULL(300," user info is null"),
    EMAIL_ERROR(301,"email format error"),
    MOBILE_ERROR(302,"mobile format error"),
    USERNAME_EXISTS(303,"username exists"),
    USER_REGISTER_ERROR(304,"user register error"),
    USERNAME_NOT_EXISTS(305,"username not exists"),
    PASSWORD_ERROR(306,"wrong password"),
    PARAMETER_NULL(307,"parameter is null"),
    NOT_LOGIN(308,"not login"),
    CART_ADD_ERROR(309,"fail to add cart"),
    PRODUCT_NOT_EXISTS(310,"product not exists"),
    PRODUCT_STOCK_ERROR(311,"stock is not enough"),
    CART_UPDATE_ERROR(312,"update cart error"),
    CART_UPDATE_PARAMETER_ERROR(313,"update cart parameter error"),
    CART_UPDATE_STOCK_ERROR(314,"update cart stock error"),
    CART_REMOVE_ERROR(315,"delete cart error"),
    ORDERS_CREATE_ERROR(316,"orders create error"),
    ORDER_DETAIL_CREATE_ERROR(317,"order detail create error"),
    USER_ADDRESS_ADD_ERROR(318,"add address error"),
    USER_ADDRESS_SET_DEFAULT_ERROR(319,"default address error");

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private Integer code;
    private String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
