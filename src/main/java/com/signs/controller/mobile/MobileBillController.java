package com.signs.controller.mobile;


import com.signs.model.commons.Result;
import com.signs.model.userPurchaseRecord.UserPurchaseRecord;
import com.signs.service.msg.MsgService;
import com.signs.service.user.UserService;
import com.signs.service.userPurchaseRecord.UserPurchaseRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app/bill")
public class MobileBillController {

    @Resource
    private UserPurchaseRecordService service;


    @GetMapping("/month/{cardNo}")
    public Result useless(@PathVariable String cardNo) {
        Result result = new Result();
        result.setData(service.selectMonth(cardNo));
        return result;
    }
    @GetMapping("/day/{userId}")
    public Result useless1(@PathVariable String userId) {
        Result result = new Result();
        result.setData(service.selectDay(userId));
        return result;
    }
}
