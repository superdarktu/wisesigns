package com.signs.service.aiarm;


import com.github.pagehelper.PageHelper;
import com.signs.mapper.aiarm.AiarmMapper;
import com.signs.mapper.watermeter.WatermeterMapper;
import com.signs.model.aiarm.Aiarm;
import com.signs.model.collector.Collector;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.waterFountains.WaterFountains;
import com.signs.model.watermeter.Watermeter;
import com.signs.service.aiarmHistory.AiarmHistoryService;
import com.signs.service.collector.CollectorService;
import com.signs.service.waterFountains.WaterFountainsService;
import com.signs.service.watermeter.WatermeterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AiarmService {

    @Resource
    private AiarmMapper mapper;

    @Resource
    private WatermeterMapper watermeterMapper;

    @Resource
    private AiarmHistoryService aiarmHistoryService;

    @Resource
    private WaterFountainsService waterFountainsService;

    @Resource
    private WatermeterService watermeterService;

    @Resource
    private CollectorService collectorService;

    /**
     * 分页查询即时报警
     *
     * @param page
     * @param start
     * @param end
     * @param keyWord
     * @return
     */
    public PageInfo<Aiarm> page(PageParam page, Date start, Date end, String keyWord, String id) {

        if (page != null && page.getPageNo() != null && page.getPageSize() != null) {
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);
        map.put("keyWord", keyWord);
        map.put("id", id);
        return new PageInfo(mapper.pageData(map));
    }

    /**
     * 报警信息统计
     *
     * @param id
     * @return
     */
    public Map<String, Object> detailed(String id) {

        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map2 = new HashMap<String, Object>();
        Map<String, Object> map3 = new HashMap<String, Object>();
        Map<String, Object> map4 = new HashMap<String, Object>();

        Integer dayAiarm = mapper.countDay(id);
        Integer monthAiarm = mapper.countMonth(id);
        Integer monthDevice = mapper.countMonthDevcie(id);
        Integer dayWarn = aiarmHistoryService.getAiarmDayCount(id);
        Integer monthWarn = aiarmHistoryService.getAiarmMonthCount(id);
        Integer monthFix = aiarmHistoryService.getMonthFixData(id);
        Integer monthAirmDevice = aiarmHistoryService.getDeviceMonthData(id);
        Integer devices = watermeterMapper.countByUser(id);
        Integer daiFix = aiarmHistoryService.getDayFixData(id);

        map1.put("月总报警条数", monthAiarm + monthWarn);
        map1.put("日总报警条数", dayAiarm + dayWarn);
        map.put("今日报警统计", map1);

        map2.put("待处理", monthAiarm);
        map2.put("当月总数报警", monthAiarm + monthWarn);
        map.put("待处理统计", map2);

        map3.put("月报警数", monthDevice + monthAirmDevice);
        map3.put("终端总数", devices);
        map.put("故障报警率", map3);

        map4.put("已处理", monthFix);
        map4.put("当月总报警数", monthAiarm + monthWarn);
        map.put("报警处理率", map4);

        return map;
    }


    public void create(String watermeterCode){

        WaterFountains waterFountains = waterFountainsService.getByWaterCode(watermeterCode);
        Watermeter watermeter = watermeterService.queryByCode(watermeterCode);
        Collector collector = collectorService.queryByCode(watermeter.getCollectorCode());
        Aiarm aiarm = new Aiarm();
        aiarm.setCtime(new Date());
        aiarm.setId(java.util.UUID.randomUUID().toString().replace("-", ""));
        aiarm.setLatitude(waterFountains.getLatitude());
        aiarm.setLongitude(waterFountains.getLongitude());
        aiarm.setPlace(waterFountains.getPlace());
        aiarm.setPropertyId(collector.getPropertyId());
        aiarm.setPropertyName(collector.getPropertyName());
        mapper.insert(aiarm);
    }

    public Aiarm queryByCode(String code){

        WaterFountains waterFountains = waterFountainsService.getByWaterCode(code);
        Aiarm aiarm = new Aiarm();
        aiarm.setWaterFountainsCode(waterFountains.getCode());
        return mapper.selectOne(aiarm);
    }

    public boolean deleteById(String id){

        return  mapper.deleteByPrimaryKey(id) > 0 ? true : false;
    }

}
