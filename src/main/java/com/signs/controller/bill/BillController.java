package com.signs.controller.bill;

import com.signs.dto.commonSearch.SearchVO;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.service.bill.BillService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;


@RestController
@RequestMapping("/api/billlist")
public class BillController {

    @Resource
    private BillService service;

    @PostMapping("/svnStatus")
    public Result getBills(PageParam param, Integer type, String value) {
        Result result = new Result();
        try {
            result.setResult(0);
            result.setData(service.page(param, type, value));
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }
    @PostMapping("/dataInquiry")
    public Result pageMonth(SearchVO vo){

        Result result = new Result();
        try {
            result.setData(service.pageMonth(vo.getId(), vo.getDate1()));
            result.setResult(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }
}
