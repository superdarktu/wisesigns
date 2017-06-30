package com.signs.service.aiarm;


import com.github.pagehelper.PageHelper;
import com.signs.mapper.aiarm.AiarmMapper;
import com.signs.model.aiarm.Aiarm;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AiarmService {

    @Resource
    private AiarmMapper mapper;

    /**
     * 分页查询即时报警
     * @param page
     * @param start
     * @param end
     * @param keyWord
     * @return
     */
    public PageInfo<Aiarm> page(PageParam page, Date start, Date end, String keyWord){

        if (page != null && page.getPageNo() != null && page.getPageSize() != null) {
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        }

        Map<String,Object> map = new HashMap<>();
        map.put("start",start);
        map.put("end",end);
        map.put("keyWord",keyWord);

        return new PageInfo(mapper.pageData(map));
    }
}
