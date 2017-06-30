package com.signs.service.aiarmHistory;


import com.github.pagehelper.PageHelper;
import com.signs.mapper.aiarmHistory.AiarmHistoryMapper;
import com.signs.model.aiarmHistory.AiarmHistory;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

@Service
public class AiarmHistoryService {

    @Resource
    private AiarmHistoryMapper mapper;

    /**
     * 分页查询历史报警
     */
    public PageInfo<AiarmHistory> page(PageParam page, Date date1, Date date2, String value, String id) {

        if (page != null && page.getPageNo() != null && page.getPageSize() != null) {
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        }
        HashMap map = new HashMap();
        if (id != null) map.put("id", id);
        if (date1 != null) map.put("date1", date1);
        if (date2 != null) map.put("date2", date2);
        if (value != null) map.put("value", "%" + value + "%");
        return new PageInfo(mapper.page(map));
    }

    /**
     * 根据ids删除
     */
    @Transactional
    public boolean delete(String idstr) {
        String ids[] = idstr.split(",");
        for (String id : ids) {
            AiarmHistory aiarmHistory = mapper.selectByPrimaryKey(id);
            if (aiarmHistory == null) continue;
            mapper.delete(aiarmHistory);
        }
        return true;
    }

    /**
     * 当天故障数量
     *
     * @param id 物业id
     */
    public Integer getAiarmDayCount(String id) {
        return mapper.getDayData(id);
    }

    /**
     * 当月故障数量
     *
     * @param id 物业id
     */
    public Integer getAiarmMonthCount(String id) {
        return mapper.getMonthData(id);
    }

    /**
     * 当月故障设备数量
     *
     * @param id 物业id
     */
    public Integer getDeviceMonthData(String id) {
        return mapper.getDeviceMonthData(id);
    }

    /**
     * 当月修复数量
     *
     * @param id 物业id
     */
    public Integer getMonthFixData(String id) {
        return mapper.getMonthFixData(id);
    }

    /**
     * 当日修复数量
     *
     * @param id 物业id
     */
    public Integer getDayFixData(String id) {
        return mapper.getDayFixData(id);
    }

}
