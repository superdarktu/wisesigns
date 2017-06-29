package com.signs.mapper.userPurchaseRecord;

import com.signs.model.userPurchaseRecord.UserPurchaseRecord;
import com.signs.util.MyMapper;

import java.util.HashMap;
import java.util.List;

public interface UserPurchaseRecordMapper extends MyMapper<UserPurchaseRecord> {
    List<UserPurchaseRecord> selectByUserId(HashMap hashMap);

    List<UserPurchaseRecord> getWaterAndMoneyDay(HashMap hashMap);

    List<UserPurchaseRecord> getWaterAndMoneyMonth(HashMap hashMap);

//    List<UserPurchaseRecord> userCount(HashMap hashMap);

    Integer totalCount(HashMap hashMap);

    UserPurchaseRecord totalDay(HashMap hashMap);

    UserPurchaseRecord totalMonth(HashMap hashMap);


}