package com.signs.controller.mobile;


import com.signs.model.commons.Result;
import com.signs.service.msg.MsgService;
import com.signs.service.user.UserService;
import com.signs.service.userPurchaseRecord.UserPurchaseRecordService;
import com.signs.service.userRechargeRecord.UserRechargeRecordService;
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

    @Resource
    private UserRechargeRecordService userRechargeRecordService;

    @Resource
    private UserPurchaseRecordService userPurchaseRecordService;





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
