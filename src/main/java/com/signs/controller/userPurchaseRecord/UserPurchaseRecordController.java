package com.signs.controller.userPurchaseRecord;

import com.signs.dto.commonSearch.SearchVO;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.service.userPurchaseRecord.UserPurchaseRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/inventory")
public class UserPurchaseRecordController {

    @Resource
    private UserPurchaseRecordService service;

    /**
     * 查询消费
     */
    @PostMapping("/consumption")
    public Result pageCard(PageParam param, SearchVO dto) {
        Result result = new Result();
        try {
            result.setData(service.page(param,dto.getId(),dto.getDate1(),dto.getDate2(),dto.getValue()));
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }
}
