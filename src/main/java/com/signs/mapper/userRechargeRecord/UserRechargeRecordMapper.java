package com.signs.mapper.userRechargeRecord;

import com.signs.model.userRechargeRecord.UserRechargeRecord;
import com.signs.util.MyMapper;

import java.util.List;

public interface UserRechargeRecordMapper extends MyMapper<UserRechargeRecord> {
    List<UserRechargeRecord>  selectByUserId(String userId);
}