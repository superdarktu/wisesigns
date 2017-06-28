package com.signs.controller.consume;

import com.signs.dto.commonSearch.SearchVO;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.service.userPurchaseRecord.UserPurchaseRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/reports")
public class ConsumeController {

    @Resource
    private UserPurchaseRecordService service;

    /**
     * 查询用户数量，当天水流量，当月水流量，当日消费金额，当月消费金额
     */
    @PostMapping("/getDate")
    public Result pageCard() {
        Result result = new Result();
        try {

            result.setResult(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }

//    /**
//     * 查询消费
//     */
//    @PostMapping("/getUserCount")
//    public Result  getUserCount(SearchVO vo){
//        Result result = new Result();
//        try {
//            result.setData(service.getUserCount(vo.getDate1()));
//            result.setResult(0);
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setResult(1);
//        }
//        return result;
//
//    }
}
