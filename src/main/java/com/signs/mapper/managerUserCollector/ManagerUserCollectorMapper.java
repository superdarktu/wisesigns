package com.signs.mapper.managerUserCollector;

import com.signs.model.collector.Collector;
import com.signs.model.managerUserCollector.ManagerUserCollector;
import com.signs.util.MyMapper;

import java.util.List;

public interface ManagerUserCollectorMapper extends MyMapper<ManagerUserCollector> {


    Integer deleteByManager(String managerId);
}