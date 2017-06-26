package com.signs.service.userPurchaseRecord;

import com.github.pagehelper.PageHelper;
import com.signs.mapper.userPurchaseRecord.UserPurchaseRecordMapper;
import com.signs.model.commons.PageParam;
import com.signs.model.userPurchaseRecord.UserPurchaseRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class UserPurchaseRecordService {

    @Resource
    private UserPurchaseRecordMapper mapper;

    /**
     * 分页查询
     */
    public List<UserPurchaseRecord>
    page(PageParam page, String userId, Date date1,Date date2,String value) {
        if (page.getPageNo() != null && page.getPageSize() != null) PageHelper.startPage(page.getPageNo(), page.getPageSize());
        HashMap hashMap=new HashMap();
        if (userId != null) hashMap.put("userId",userId);
        if (date1 != null) hashMap.put("date1",date1);
        if (date2 != null) hashMap.put("date2",date2);
        if (value != null) hashMap.put("value","%" + value + "%");
        return mapper.selectByUserId(hashMap);
    }

}
