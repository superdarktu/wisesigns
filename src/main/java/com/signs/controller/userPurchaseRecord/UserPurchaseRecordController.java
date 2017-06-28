package com.signs.controller.userPurchaseRecord;

import com.signs.dto.commonSearch.SearchVO;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.service.userPurchaseRecord.UserPurchaseRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/api/consume")
public class UserPurchaseRecordController {

    @Resource
    private UserPurchaseRecordService service;



}
