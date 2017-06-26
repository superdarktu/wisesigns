package com.signs.controller.waterCard;

import com.signs.model.commons.PageParam;
import com.signs.service.userPurchaseRecord.UserPurchaseRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/api/inventory")
public class WaterCardController {

    @Resource
    private UserPurchaseRecordService service;


    /**
     * 查询水卡
     */
    @PostMapping("/svnStatus")
    public Object pageCard(PageParam param, String userId, Date date1, Date date2,String value) {
        try {
        return service.page(param,userId,date1,date2,value);
        } catch (Exception e) {
            e.printStackTrace();
            return "1";
        }
    }
}
