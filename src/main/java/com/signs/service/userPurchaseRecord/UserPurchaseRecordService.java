package com.signs.service.userPurchaseRecord;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.signs.dto.waterCard.CardDto;
import com.signs.mapper.collector.CollectorMapper;
import com.signs.mapper.userPurchaseRecord.UserPurchaseRecordMapper;
import com.signs.mapper.waterFountains.WaterFountainsMapper;
import com.signs.model.collector.Collector;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.userPurchaseRecord.UserPurchaseRecord;
import com.signs.model.waterFountains.WaterFountains;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserPurchaseRecordService {

    @Resource
    private UserPurchaseRecordMapper mapper;
    @Resource
    private WaterFountainsMapper waterFountainsMapper;
    @Resource
    private CollectorMapper collectorMapper;

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
     * 查询当月消费账单
     */
    public Float selectMonth(String cardNo) {
        HashMap hashMap = new HashMap();
        if (cardNo != null) {
            hashMap.put("cardNo", cardNo);
        }
        UserPurchaseRecord userPurchaseRecord = mapper.selectMonth(hashMap);
        if (userPurchaseRecord == null) {
            return 0f;
        }
        return userPurchaseRecord.getBalance();
    }

    /**
     * 最后一条记录
     */
    public UserPurchaseRecord selectDay(String userId) {
        HashMap hashMap = new HashMap();
        if (userId != null) {
            hashMap.put("userId", userId);
        }
        return mapper.selectDay(hashMap);
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

        Float price = divide.getTotalPrice();//总收入
        Float price1 = divide.getPrice();//物业收入
        hashMap.put("type", 2);
        UserPurchaseRecord divide1 = mapper.divide1(hashMap);
        Float price2 = divide1.getPrice();

        hashMap.put("type", 3);
        UserPurchaseRecord divide2 = mapper.divide1(hashMap);
        Float price3 = divide2.getPrice();


        object.put("total", price);
        object.put("property", price1);
        object.put("invest", price2);
        object.put("spread", price3);

        return object;
    }

    public List<UserPurchaseRecord> selectAll(String cardNo, Integer type, Date date) {
        Map map = new HashMap();
        if (cardNo != null) {
            map.put("cardNo", cardNo);
        }
        if (date != null) {
            map.put("date", date);
        }
        if (type != null) {
            map.put("type", type);
        } else {
            map.put("type", 1);
        }

        return mapper.selectAllOrder(map);
    }

    public UserPurchaseRecord selectOneOrder(String orderId) {
        return mapper.selectOneOrder(orderId);
    }

    public JSONObject selectDefaultYear(String defaultCardNo, Integer type) {
        Map map = new HashMap();
        map.put("defaultCardNo", defaultCardNo);
        if (type != null) {
            map.put("type", type);
        } else {
            map.put("type", 1);
        }
        List<UserPurchaseRecord> list = mapper.selectDefaultYear(map);


        JSONObject jmap = new JSONObject();
        JSONArray array1 = new JSONArray();
        JSONArray array2 = new JSONArray();

        for (int i = 1; i < new Date().getMonth() + 2; i++) {
            boolean flag = false;
            for (UserPurchaseRecord bill : list) {
                if (("" + i).equals(bill.getName())) {
                    //name代表月份，price消费
                    array1.add(bill.getName() + "月");
                    array2.add(bill.getPrice());
                    flag = true;
                }
            }
            if (!flag) {
                array1.add(i + "月");
                array2.add(0);
            }
        }

        jmap.put("date", array1);
        jmap.put("data", array2);
        return jmap;
    }



    public boolean createUserPurchaseRecord(UserPurchaseRecord record, String watermeterCode) {
        String id = UUID.randomUUID().toString().replace("-", "");
        record.setId(id);
        WaterFountains fountains = waterFountainsMapper.selectTableCode(watermeterCode).get(0);
        record.setPlace(fountains.getPlace());
        record.setCtime(new Date());
        record.setCollectorId(collectorMapper.selectCollectorIdByMeterCode(watermeterCode));
        mapper.insert(record);


        return false;
    }
}
