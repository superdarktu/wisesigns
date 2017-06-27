package com.signs.controller.bill;

import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.service.bill.BillService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/billlist")
public class BillController {

    @Resource
    private BillService service;

    @PostMapping("/svnStatus")
    public Result getBills(PageParam param, Integer type, String value) {
        Result result = new Result();
        try {
            result.setData(service.page(param, type, value));
        } catch (Exception e) {
            result.setData("1");
        }
        return result;

    }
}
