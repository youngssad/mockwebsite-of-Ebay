package com.example.controller;

import com.example.entity.User;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class RedirectController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{url}")
    public ModelAndView redirect(@PathVariable("url") String url, HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(url);
        //check if user is logged in
        User user = (User) session.getAttribute("user");
        if(user == null){
            //not logged in
            modelAndView.addObject("cartList", new ArrayList<>());
        }else{
            //logged in
            //check user cart list
            modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
        }
        return modelAndView;
    }

    @GetMapping("/")
    public String main(){
        return "redirect:/productCategory/main";
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

}