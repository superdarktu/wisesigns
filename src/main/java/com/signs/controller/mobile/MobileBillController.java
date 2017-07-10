package com.signs.controller.mobile;


import com.signs.dto.waterCard.CardDto;
import com.signs.model.commons.Result;
import com.signs.service.userPurchaseRecord.UserPurchaseRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app/bill")
public class MobileBillController {

    @Resource
    private UserPurchaseRecordService service;


    /**
     * 获得卡号下的订单
     * @param dto
     * @return
     */
    @GetMapping("/all")
    public Result useless3(CardDto dto) {
        Result result = new Result();
        result.setData(service.selectAll(dto.getCardNo(),dto.getType(),dto.getDate()));
        return result;
    }

    /**
     * 某个订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/detail/{orderId}")
    public Result useless4(@PathVariable String orderId) {
        Result result = new Result();
        result.setData(service.selectOneOrder(orderId));
        return result;
    }

    /**
     * 12个月的数据
     * @param defaultCardNo
     * @return
     */
    @GetMapping("/defaultYear/{defaultCardNo}")
    public Result useless5(@PathVariable String defaultCardNo) {
        Result result = new Result();
        result.setData(service.selectDefaultYear(defaultCardNo));
        return result;
    }
}
