package com.signs.mapper.userPurchaseRecord;

import com.signs.model.userPurchaseRecord.UserPurchaseRecord;
import com.signs.util.MyMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserPurchaseRecordMapper extends MyMapper<UserPurchaseRecord> {
    List<UserPurchaseRecord> selectByUserId(HashMap hashMap);

    List<UserPurchaseRecord> getWaterAndMoneyDay(HashMap hashMap);

    List<UserPurchaseRecord> getWaterAndMoneyMonth(HashMap hashMap);


    Integer totalCount(HashMap hashMap);

    UserPurchaseRecord totalDay(HashMap hashMap);

    UserPurchaseRecord totalMonth(HashMap hashMap);


    UserPurchaseRecord divide(HashMap hashMap);

    UserPurchaseRecord divide1(HashMap hashMap);


    UserPurchaseRecord selectMonth(HashMap hashMap);

    UserPurchaseRecord selectDay(HashMap hashMap);

    List<UserPurchaseRecord> selectAllOrder(Map map);

    List<UserPurchaseRecord> selectDefaultYear(Map map);

    UserPurchaseRecord selectOneOrder(String orderId);
}