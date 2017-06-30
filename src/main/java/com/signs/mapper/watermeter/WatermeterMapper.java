package com.signs.mapper.watermeter;

import com.signs.model.watermeter.Watermeter;
import com.signs.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface WatermeterMapper extends MyMapper<Watermeter> {

    /**
     * 分页条件查询
     */
    List<Watermeter> pageData(Map<String, Object> map);

    List<String> queryByCollector(String collectorCode);

    Watermeter isHaveCode(String code);

    Integer countByUser(String id);
}