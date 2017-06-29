package com.signs.service.userPurchaseRecord;

import com.alibaba.fastjson.JSONObject;
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
    page(PageParam page, String id, Date date1, Date date2, String value) {
        if (page.getPageNo() != null && page.getPageSize() != null)
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        HashMap hashMap = new HashMap();
        if (id != null) hashMap.put("id", id);
        if (date1 != null) hashMap.put("date1", date1);
        if (date2 != null) hashMap.put("date2", date2);
        if (value != null) hashMap.put("value", "%" + value + "%");
        return mapper.selectByUserId(hashMap);
    }

    /**
     * 统计人数
     */
    public JSONObject getUserCount(Date date) {
        HashMap hashMap = new HashMap();
        hashMap.put("date",date);
        List<UserPurchaseRecord> userCount = mapper.userCount(hashMap);
        JSONObject jmap=new JSONObject();
        for (UserPurchaseRecord record : userCount) {
            jmap.put(record.getName(),record.getPrice());
        }
        return jmap;
    }

    /**
     * 查询用户数量，当天水流量，当月水流量，当日消费金额，当月消费金额
     */
    public JSONObject getTotal(Date date,String id){
        HashMap hashMap=new HashMap();
        JSONObject jmap=new JSONObject();
        if (date == null) {
            date=new Date();
        }
        hashMap.put("date",date);
        if (id != null) {
            hashMap.put("id",id);
        }
        Integer userCount = mapper.totalCount(hashMap);
        UserPurchaseRecord day = mapper.totalDay(hashMap);//天
        UserPurchaseRecord month = mapper.totalMonth(hashMap);//月
        jmap.put("1",userCount);
        jmap.put("2", day.getWaterConsumption());
        jmap.put("3",month.getWaterConsumption());
        jmap.put("4",day.getUnitCost());
        jmap.put("5", month.getUnitCost());
        return jmap;
    }

}
