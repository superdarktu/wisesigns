package com.signs.controller.mobile;


import com.signs.model.commons.Result;
import com.signs.model.user.User;
import com.signs.model.userRechargeRecord.UserRechargeRecord;
import com.signs.service.user.UserService;
import com.signs.service.userRechargeRecord.UserRechargeRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/app/recharge")
public class MobileRechargeRecordController {

    @Resource
    private UserRechargeRecordService service;

    @Resource
    private UserService userService;


    @PostMapping("create")
    public Result create(Float price, String cardNo, String orderId, HttpSession session){

        Result result  =  new Result();
        try {

            String id = session.getAttribute("id").toString();
            User user = userService.queryById(id);
            UserRechargeRecord userRechargeRecord = new UserRechargeRecord();
            userRechargeRecord.setCardId(cardNo);
            userRechargeRecord.setOrderId(orderId);
            userRechargeRecord.setUserId(id);
            userRechargeRecord.setName(user.getName());
            if(service.create(userRechargeRecord)){

                result.setResult(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  result;
    }
}
