package com.signs.mapper.collector;

import com.signs.dto.Collector.CollectorVO;
import com.signs.model.collector.Collector;
import com.signs.util.MyMapper;

import java.util.List;

public interface CollectorMapper extends MyMapper<Collector> {

    List<CollectorVO> findData(Collector collector);

    List<Collector> findByManager(String managerId);

    List<Collector> findByOther(String managerId);


    List<Collector> findWithNoProperty();

    Collector isHaveCode(String code);

   Integer updateProperty(String id);

}