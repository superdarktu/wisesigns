package com.signs.mapper.managerUserCollector;

import com.signs.model.managerUserCollector.ManagerUserCollector;
import com.signs.util.MyMapper;

public interface ManagerUserCollectorMapper extends MyMapper<ManagerUserCollector> {


    Integer deleteByManager(String managerId);
}