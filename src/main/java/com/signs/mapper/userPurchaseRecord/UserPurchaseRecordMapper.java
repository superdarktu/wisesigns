package com.signs.mapper.userPurchaseRecord;

import com.signs.model.userPurchaseRecord.UserPurchaseRecord;
import com.signs.util.MyMapper;

import java.util.HashMap;
import java.util.List;

public interface UserPurchaseRecordMapper extends MyMapper<UserPurchaseRecord> {
    List<UserPurchaseRecord> selectByUserId(HashMap hashMap);


    List<UserPurchaseRecord> userCount(HashMap hashMap);

}