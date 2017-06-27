package com.signs.controller.user;

import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.model.commons.TwoDataResult;
import com.signs.service.user.UserService;
import com.signs.service.userPurchaseRecord.UserPurchaseRecordService;
import com.signs.service.userRechargeRecord.UserRechargeRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/endUser")
public class EndUserController {

    @Resource
    private UserService service;

    @Resource
    private UserPurchaseRecordService service1;
    @Resource
    private UserRechargeRecordService service2;

    /**
     * 查询终端用户
     */
    @PostMapping("/svnStatus")
    public Result pageUser(PageParam param, String status, String value) {
        Result result = new Result();
        try {
            result.setData(service.page(param, status, value));

        } catch (Exception e) {
            e.printStackTrace();
            result.setData("1");
        }
        return result;
    }

    /**
     * 查询该用户的账单
     */
    @PostMapping("/billInquiry")
    public TwoDataResult pageBill(PageParam param, String id) {
        TwoDataResult result = new TwoDataResult();
        try {
            result.setConsume(service1.page(param, id, null, null, null));
            result.setRecharge(service2.page(param, id, null, null, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
