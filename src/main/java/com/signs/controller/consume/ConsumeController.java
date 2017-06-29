package com.signs.controller.consume;

import com.signs.dto.commonSearch.SearchVO;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.model.userRechargeRecord.UserRechargeRecord;
import com.signs.service.userPurchaseRecord.UserPurchaseRecordService;
import com.signs.service.userRechargeRecord.UserRechargeRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/reports")
public class ConsumeController {

    @Resource
    private UserPurchaseRecordService service;
    @Resource
    private UserRechargeRecordService service1;

    /**
     * 查询用户数量，当天水流量，当月水流量，当日消费金额，当月消费金额
     */
    @PostMapping("/getData")
    public Result pageCard(SearchVO vo) {
        Result result = new Result();
        try {
          result.setData(service.getTotal(vo.getDate1(), vo.getId()));
            result.setResult(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }
    /**
     * 统计
     */
    @PostMapping("/concrete")
    public Result getUserCount(HttpSession session, SearchVO vo, Integer type) {
        Result result = new Result();
        try {
            String id = (String) session.getAttribute("id");
            result.setData(service.getUserCount(id,vo.getDate1(),type));
            result.setResult(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }
    /**
     * 统计
     */
    @PostMapping("/cumulative")
    public Result getRecharge(HttpSession session, SearchVO vo, Integer type) {
        Result result = new Result();
        try {
            String id = (String) session.getAttribute("id");
            result.setData(service1.getUserCount(id,vo.getDate1(),type));
            result.setResult(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }

}
