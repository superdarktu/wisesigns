package com.signs.service.watermeter;

import com.github.pagehelper.PageHelper;
import com.signs.mapper.watermeter.WatermeterMapper;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.waterCard.WaterCard;
import com.signs.model.watermeter.Watermeter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WatermeterService {

    @Resource
    private WatermeterMapper mapper;


    /**
     * 根据ids删除
     *
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
     * 添加水表
     *
     * @param watermeter
     * @return
     */
    public boolean insert(Watermeter watermeter) {

        watermeter.setId(java.util.UUID.randomUUID().toString().replace("-", ""));
        watermeter.setCtime(new Date());
        watermeter.setStatus(0);
        watermeter.setFlowDay(0);
        watermeter.setFlowMonth(0);
        watermeter.setTapStatus(1);
        if (mapper.insert(watermeter) > 0) return true;
        return false;
    }

    /**
     * 修改水表
     *
     * @param watermeter
     * @return
     */
    public boolean update(Watermeter watermeter) {

        if (mapper.updateByPrimaryKeySelective(watermeter) > 0) return true;

        return false;
    }

    /**
     * 分页条件查询
     *
     * @param page
     * @param map
     * @return
     */
    public PageInfo<Watermeter> page(PageParam page, Map<String, Object> map) {

        if (page != null && page.getPageNo() != null && page.getPageSize() != null) {
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        }

        return new PageInfo<Watermeter>(mapper.pageData(map));
    }

    /**
     * 根据id查询水表信息
     *
     * @param id
     * @return
     */
    public Watermeter query(String id) {

        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 修改水表阀门开关
     *
     * @param id
     * @return
     */
    public boolean changeTap(String id) {

        Watermeter watermeter = mapper.selectByPrimaryKey(id);

        Assert.notNull(watermeter, "该ID水表不存在");
        watermeter.setTapStatus(watermeter.getTapStatus() == 0 ? 1 : 0);

        return mapper.updateByPrimaryKeySelective(watermeter) > 0;
    }

    /**
     * 根据采集器获取所有的表编号
     *
     * @param collectorCode
     * @return
     */
    public List<String> queryByCollector(String collectorCode) {

        return mapper.queryByCollector(collectorCode);
    }

    /**
     * 编号唯一
     *
     * @param code
     * @return
     */
    public boolean isHaveCode(String code) {

        return mapper.isHaveCode(code) != null;
    }

    /**
     *
     * @param code
     * @return
     */
    public Watermeter queryByCode(String code){

        return mapper.isHaveCode(code);
    }


}
