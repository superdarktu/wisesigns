package com.signs.service.dataCollectors;


import com.alibaba.fastjson.JSONObject;
import com.signs.mapper.dataCollectors.DataCollectorsMapper;
import com.signs.model.dataCollectors.DataCollectors;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class DataCollectorsService {

    @Resource
    private DataCollectorsMapper mapper;

    public void create(JSONObject object){

        DataCollectors dataCollectors = new DataCollectors();
        dataCollectors.setCode(object.get("采集器编号").toString());

        if(mapper.select(dataCollectors).size()>0)
            return;
        dataCollectors.setTime(new Date());
        mapper.insert(dataCollectors);
    }
}
