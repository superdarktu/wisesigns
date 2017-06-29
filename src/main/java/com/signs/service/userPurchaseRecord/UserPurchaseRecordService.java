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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        if (id != null) hashMap.put("id", id);
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
        jmap.put("用户数量", userCount);
        jmap.put("今日用水量", day.getWaterConsumption());
        jmap.put("当月用水量", month.getWaterConsumption());
        jmap.put("今日消费金额", day.getUnitCost());
        jmap.put("当月消费金额", month.getUnitCost());
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
            arr1.add(record.getName()+":00");//日期
            arr2.add(record.getPrice());//消费金额
            arr3.add(record.getTotalPrice());//人数
            arr4.add(record.getWaterConsumption());//用水量
        }
        day.put("几点",arr1);
        day.put("消费金额",arr2);
        day.put("人数",arr3);
        day.put("用水量",arr4);


//        for (int i = 0; i < 24; i++) {
//            boolean flag = false;
//            for (UserPurchaseRecord record : waterAndMoneyDay) {
//                if (("" + i).equals(record.getName())) {
//                    array2.add(record.getWaterConsumption());
//                    array3.add(record.getPrice());
//                    flag = true;
//                }
//            }
//            if (!flag) {
//                array2.add(0);
//                array3.add(0);
//            }
//        }
//        for (UserPurchaseRecord userPurchaseRecord : waterAndMoneyDay) {
//            String name = userPurchaseRecord.getName();//时间
//            Float waterConsumption = userPurchaseRecord.getWaterConsumption();//水量
//            Float price = userPurchaseRecord.getPrice();//金额
//
//            jmap1.put(name, waterConsumption);
//            jmap2.put(name, price);
//
//        }
//        每月数据
        List<UserPurchaseRecord> waterAndMoneyMonth = mapper.getWaterAndMoneyMonth(hashMap);


//        for (int i = 1; i < 32; i++) {
//            boolean flag = false;
//            for (UserPurchaseRecord record : waterAndMoneyMonth) {
//                if (("" + i).equals(record.getName())) {
//                    jmap3.put(record.getName(), record.getWaterConsumption());
//                    jmap4.put(record.getName(), record.getPrice());
//                    flag = true;
//                }
//            }
//            if (!flag) {
//                jmap3.put("" + i, 0);
//                jmap4.put("" + i, 0);
//            }
//        }
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
        month.put("几号", array4);
        month.put("月流水量", array5);
        month.put("月消费金额", array6);
        object.put("当天用量",day);
        object.put("当月用量", month);

        return object;
    }


}
