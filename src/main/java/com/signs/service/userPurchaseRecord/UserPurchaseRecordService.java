package com.signs.service.userPurchaseRecord;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.signs.mapper.userPurchaseRecord.UserPurchaseRecordMapper;
import com.signs.model.commons.PageInfo;
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
    public PageInfo<UserPurchaseRecord>
    page(PageParam page, String id, Date date1, Date date2, String value) {
        if (page.getPageNo() != null && page.getPageSize() != null)
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        HashMap hashMap = new HashMap();
        if (id != null) hashMap.put("userId", id);
        if (date1 != null) hashMap.put("date1", date1);
        if (date2 != null) hashMap.put("date2", date2);
        if (value != null) hashMap.put("value", "%" + value + "%");
        return new PageInfo(mapper.selectByUserId(hashMap));
    }

    /**
     * 查询用户数量，当天水流量，当月水流量，当日消费金额，当月消费金额
     */
    public JSONObject getTotal(Date date, String id) {
        HashMap hashMap = new HashMap();
        JSONObject jmap = new JSONObject();
        if (date == null) {
            date = new Date();
        }
        hashMap.put("date", date);
        if (id != null) {
            hashMap.put("id", id);
        }
        Integer userCount = mapper.totalCount(hashMap);
        UserPurchaseRecord day = mapper.totalDay(hashMap);//天
        UserPurchaseRecord month = mapper.totalMonth(hashMap);//月
        if (day == null) {
            day = new UserPurchaseRecord();
        }
        if (month == null) {
            month = new UserPurchaseRecord();
        }
        jmap.put("userCount", userCount);
        jmap.put("dayWater", day.getWaterConsumption());
        jmap.put("monthWater", month.getWaterConsumption());
        jmap.put("dayCost", day.getUnitCost());
        jmap.put("monthCost", month.getUnitCost());
        return jmap;
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
        List<UserPurchaseRecord> userCount = mapper.getWaterAndMoneyDay(hashMap);
        JSONObject day = new JSONObject();
        JSONArray arr1 = new JSONArray();
        JSONArray arr2 = new JSONArray();
        JSONArray arr3 = new JSONArray();
        JSONArray arr4 = new JSONArray();
        for (UserPurchaseRecord record : userCount) {
            arr1.add(record.getName() + ":00");//日期
            arr2.add(record.getPrice());//消费金额
            arr3.add(record.getTotalPrice());//人数
            arr4.add(record.getWaterConsumption());//用水量
        }
        day.put("hourTime", arr1);
        day.put("dayCost", arr2);
        day.put("userCount", arr3);
        day.put("dayWater", arr4);

//        每月数据
        List<UserPurchaseRecord> waterAndMoneyMonth = mapper.getWaterAndMoneyMonth(hashMap);
        JSONObject month = new JSONObject();
        JSONArray array4 = new JSONArray();
        JSONArray array5 = new JSONArray();
        JSONArray array6 = new JSONArray();
        for (UserPurchaseRecord userPurchaseRecord : waterAndMoneyMonth) {
            String name = userPurchaseRecord.getName();//时间
            Float waterConsumption = userPurchaseRecord.getWaterConsumption();//水量
            Float price = userPurchaseRecord.getPrice();//金额
            array4.add(name);
            array5.add(waterConsumption);
            array6.add(price);
        }
        month.put("dayTime", array4);
        month.put("monthWater", array5);
        month.put("monthCost", array6);
        object.put("day", day);
        object.put("month", month);
        return object;
    }

    /**
     * 分成
     */

    public JSONObject divide(Date date, String type) {
        JSONObject object = new JSONObject();
        HashMap hashMap = new HashMap();
        if (date == null) {
            date = new Date();
        }
        if ("month".equals(type)) {
            hashMap.put("month", 1);
        } else {
            hashMap.put("day", 2);
        }
        hashMap.put("date", date);

        UserPurchaseRecord divide = mapper.divide(hashMap);

        Float price = divide.getTotalPrice();
//                == null ? 0 : divide.getTotalPrice();//总收入
        Float price1 = divide.getPrice();
//                == null ? 0 : divide.getPrice();//物业收入
        hashMap.put("type", 2);
        UserPurchaseRecord divide1 = mapper.divide1(hashMap);
        Float price2 = divide1.getPrice();
//                == null ? 0 : divide1.getPrice();
        hashMap.put("type", 3);
        UserPurchaseRecord divide2 = mapper.divide1(hashMap);
        Float price3 = divide2.getPrice();
//                == null ? 0 : divide1.getTotalPrice();

        object.put("total", price);
        object.put("property", price1);
        object.put("invest", price2);
        object.put("spread", price3);

        return object;
    }

}
