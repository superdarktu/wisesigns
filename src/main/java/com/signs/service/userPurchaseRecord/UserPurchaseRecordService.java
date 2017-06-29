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
        List<UserPurchaseRecord> userCount = mapper.userCount(hashMap);
        JSONObject jmap = new JSONObject();
        for (UserPurchaseRecord record : userCount) {

            for (int i = 0; i < 24; i++) {
                if (("" + i).equals(record.getName())) {

                }
            }

            jmap.put(record.getName(), record.getPrice());
        }
        //每天数据
        List<UserPurchaseRecord> waterAndMoneyDay = mapper.getWaterAndMoneyDay(hashMap);
        JSONObject jmap1 = new JSONObject();
        JSONObject jmap2 = new JSONObject();
        for (UserPurchaseRecord userPurchaseRecord : waterAndMoneyDay) {
            String name = userPurchaseRecord.getName();//时间
            Float waterConsumption = userPurchaseRecord.getWaterConsumption();//水量
            Float price = userPurchaseRecord.getPrice();//金额

            jmap1.put(name, waterConsumption);
            jmap2.put(name, price);

        }
//        每月数据
        List<UserPurchaseRecord> waterAndMoneyMonth = mapper.getWaterAndMoneyMonth(hashMap);
        JSONObject jmap3 = new JSONObject();
        JSONObject jmap4 = new JSONObject();
        for (UserPurchaseRecord userPurchaseRecord : waterAndMoneyMonth) {
            String name = userPurchaseRecord.getName();//时间
            Float waterConsumption = userPurchaseRecord.getWaterConsumption();//水量
            Float price = userPurchaseRecord.getPrice();//金额
            jmap3.put(name, waterConsumption);
            jmap4.put(name, price);
        }
        object.put("用户数量", jmap);
        object.put("当日用水量", jmap1);
        object.put("当日消费金额", jmap2);
        object.put("当月用水量", jmap3);
        object.put("当月消费金额", jmap4);

        return object;
    }


}
