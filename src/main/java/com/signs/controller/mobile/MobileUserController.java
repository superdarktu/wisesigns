package com.signs.controller.mobile;


import com.signs.model.commons.Result;
import com.signs.service.msg.MsgService;
import com.signs.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app/user")
public class MobileUserController {

    @Resource
    private UserService service;

    @Resource
    private MsgService msgService;



    @GetMapping("/login")
    public Result login(){

        Result result = new Result();
        return  result;
    }

    @GetMapping("/msg")
    public Result msg(String phone){

        Result result = new Result();
        msgService.sendMsg(phone);
        return  result;
    }
}
