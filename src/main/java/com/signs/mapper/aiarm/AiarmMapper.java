package com.signs.mapper.aiarm;

import com.signs.model.aiarm.Aiarm;
import com.signs.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface AiarmMapper extends MyMapper<Aiarm> {

    List<Aiarm> pageData(Map<String,Object> map);

    Integer countDay(String id);

    Integer countMonth(String id);

    Integer countMonthDevcie(String id);
}