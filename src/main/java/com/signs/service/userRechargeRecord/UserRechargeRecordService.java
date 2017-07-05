package com.signs.service.userRechargeRecord;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.signs.mapper.userRechargeRecord.UserRechargeRecordMapper;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.userRechargeRecord.UserRechargeRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class UserRechargeRecordService {

    @Resource
    private UserRechargeRecordMapper mapper;

    public boolean create(UserRechargeRecord userRechargeRecord){

        userRechargeRecord.setId(java.util.UUID.randomUUID().toString().replace("-", ""));
        userRechargeRecord.setCtime(new Date());
        mapper.insertSelective(userRechargeRecord);
        return true;
    }

    /**
     * 分页查询
     */
    public PageInfo<UserRechargeRecord>
    page(PageParam page, String id, Date date1, Date date2, String value) {
        if (page.getPageNo() != null && page.getPageSize() != null)
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        HashMap hashMap = new HashMap();
        if (id != null) hashMap.put("id", id);
        if (date1 != null) hashMap.put("date1", date1);
        if (date2 != null) hashMap.put("date2", date2);
        if (value != null) hashMap.put("value", "%" + value + "%");
        return new PageInfo(mapper.selectByUserId(hashMap));
    }

    /**
     * 统计24小时或30天
     */
    public JSONObject getUserCount(String id, Date date, Integer type) {
        HashMap hashMap = new HashMap();
        JSONObject object = new JSONObject();
        if (id != null) {
            hashMap.put("id", id);
        }
        if (date != null) {
            hashMap.put("date", date);
        }
        if (type != null) {
            hashMap.put("type", type);
        }
        //每天数据
        List<UserRechargeRecord> waterAndMoneyDay = mapper.getWaterAndMoneyDay(hashMap);
        JSONObject day = new JSONObject();
        JSONArray arr1 = new JSONArray();
        JSONArray arr2 = new JSONArray();
        for (UserRechargeRecord record : waterAndMoneyDay) {
            arr1.add(record.getName() + ":00");//日期
            arr2.add(record.getPrice());//消费金额
        }
        day.put("hourTime", arr1);
        day.put("dayCost", arr2);

//        每月数据
        List<UserRechargeRecord> waterAndMoneyMonth = mapper.getWaterAndMoneyMonth(hashMap);
        JSONObject month = new JSONObject();
        JSONArray array1 = new JSONArray();
        JSONArray array2 = new JSONArray();
        for (UserRechargeRecord userPurchaseRecord : waterAndMoneyMonth) {
            String name = userPurchaseRecord.getName();//时间
            Float price = userPurchaseRecord.getPrice();//金额
            array1.add(name);
            array2.add(price);
        }
        month.put("dayTime", array1);
        month.put("monthCost", array2);
        object.put("day", day);
        object.put("month", month);
        return object;
    }

    /**
     * 获取最后条消费记录
     * @param userId
     * @return
     */
    public UserRechargeRecord getLast(String userId){

        return mapper.getLast(userId);
    }

    /**
     * 获取当月充值记录
     * @param cardNo
     * @return
     */
    public Float getMonthPrice(String cardNo){

        return mapper.getMonthPrice(cardNo);
    }


}
