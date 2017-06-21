package com.signs.service.collector;

import com.github.pagehelper.PageHelper;
import com.signs.dto.Collector.CollectorVO;
import com.signs.mapper.collector.CollectorMapper;
import com.signs.model.collector.Collector;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CollectorService {

    @Resource
    private CollectorMapper mapper;


    /**
     * 添加采集器
     * @param collector
     * @return
     */
    public boolean insert(Collector collector){

        collector.setCtime(new Date());
        collector.setId(java.util.UUID.randomUUID().toString().replace("-", ""));
        collector.setStatus(0);
        mapper.insert(collector);
        return true;
    }

    /**
     * 修改采集器
     * @param collector
     * @return
     */
    public boolean update(Collector collector){

        return mapper.updateByPrimaryKeySelective(collector) > 0 ? true : false;
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public Collector query(String id){

        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 删除采集器
     * @param idstr
     * @return
     */
    @Transactional
    public boolean delete(String idstr) {

        String ids[] = idstr.split(",");
        for (String id : ids) {
            mapper.deleteByPrimaryKey(id);
        }
        return true;
    }

    /**
     * 分页条件查询
     * @param page
     * @param collector
     * @return
     */
    public PageInfo<CollectorVO> page(PageParam page, Collector collector) {

        if (page.getPageNo() != null && page.getPageSize() != null) {
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        }
        List<CollectorVO> result = mapper.findData(collector);
        return new PageInfo(result);
    }
}
