package com.signs.mapper.aiarmHistory;

import com.signs.model.aiarmHistory.AiarmHistory;
import com.signs.util.MyMapper;

import java.util.HashMap;
import java.util.List;

public interface AiarmHistoryMapper extends MyMapper<AiarmHistory> {

    List<AiarmHistory> page(HashMap hashMap);

    Integer getDayData(String id);

    Integer getMonthData(String id);

    Integer getMonthFixData(String id);
    Integer getDeviceMonthData(String id);

}