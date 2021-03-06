package com.signs.mapper.userRechargeRecord;

import com.signs.model.userRechargeRecord.UserRechargeRecord;
import com.signs.util.MyMapper;

import java.util.HashMap;
import java.util.List;

public interface UserRechargeRecordMapper extends MyMapper<UserRechargeRecord> {
    List<UserRechargeRecord> selectByUserId(HashMap hashMap);

    List<UserRechargeRecord> getWaterAndMoneyDay(HashMap hashMap);

    List<UserRechargeRecord> getWaterAndMoneyMonth(HashMap hashMap);

    UserRechargeRecord getLast(String userId);

    Float getMonthPrice(String cardNo);
}