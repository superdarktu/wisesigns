package com.signs.service.waterFountains;

import com.github.pagehelper.PageHelper;
import com.signs.mapper.waterFountains.WaterFountainsMapper;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.waterFountains.WaterFountains;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class WaterFountainsService {

    @Resource
    private WaterFountainsMapper mapper;


    /**
     * 创饮水机
     */
    @Transactional
    public boolean createFountains(String waterPosition, String tableNumber, Integer waterType, Float longitude, Float latitude) {
        WaterFountains lastDispenser = mapper.getLastDispenser();
        String waterNumber = lastDispenser == null ? "0" : "" + (Integer.parseInt(lastDispenser.getCode()) + 1);
        List<WaterFountains> fountains = mapper.selectCode(waterNumber);
        if (fountains == null || fountains.size() > 0) return false;
        if (mapper.selectTableCode(tableNumber).size() > 0) return false;
        WaterFountains waterFountains = new WaterFountains();
        waterFountains.setId(java.util.UUID.randomUUID().toString().replace("-", ""));
        waterFountains.setCtime(new Date());
        waterFountains.setCode(waterNumber);
        waterFountains.setTableCode(tableNumber);
        waterFountains.setPlace(waterPosition);
        waterFountains.setLatitude(latitude);
        waterFountains.setLongitude(longitude);
        waterFountains.setType(waterType);
        mapper.insert(waterFountains);
        return true;
    }


    /**
     * 饮水机编号唯一
     */
    public boolean selectCode(String waterNumber) {
        List<WaterFountains> waterFountains = mapper.selectCode(waterNumber);
        return waterFountains.size() > 0;
    }


    /**
     * 根据ids删除
     */
    @Transactional
    public boolean delete(String idstr) {

        String ids[] = idstr.split(",");
        for (String id : ids) {
            WaterFountains waterFountains = mapper.selectByPrimaryKey(id);
            if (waterFountains == null) continue;
            mapper.delete(waterFountains);
        }
        return true;
    }

    /**
     * 查询修改的饮水机信息
     */
    public WaterFountains gain(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 修改饮水机信息
     */
    public WaterFountains save(String id, String newWaterNumber, String newWaterPosition, String newTableNumber, Integer newWaterType, Float newLongitude, Float newLatitude) {
//        卡号不重复
        WaterFountains fountains = mapper.selectByPrimaryKey(id);
        if (fountains == null) return null;
        WaterFountains waterFountains = new WaterFountains();
        waterFountains.setId(id);
        waterFountains.setCtime(new Date());
        waterFountains.setCode(newWaterNumber);
        waterFountains.setTableCode(newTableNumber);
        waterFountains.setPlace(newWaterPosition);
        waterFountains.setLatitude(newLatitude);
        waterFountains.setLongitude(newLongitude);
        waterFountains.setType(newWaterType);
        mapper.updateByPrimaryKeySelective(waterFountains);

        return waterFountains;
    }

    /**
     * 分页查询
     */
    public PageInfo<WaterFountains>
    page(PageParam page, String type, String value) {
        if (page != null && page.getPageNo() != null && page.getPageSize() != null)
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        HashMap<String, String> hashMap = new HashMap<>();
        if (type != null) hashMap.put("type", type);
        if (value != null) hashMap.put("value", "%" + value + "%");
        return new PageInfo(mapper.getDispenser(hashMap));
    }

    /**
     * 单个饮水机
     */
    public WaterFountains getSingleWaterFountains(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询公用
     */
    public List<WaterFountains> getPublicWaterFountains() {
        return page(null, "1", null).getList();
    }

    /**
     * 表编号正确
     */
    public Integer validate(String id) {
        String s = mapper.tableCodeUsed(id);
        String s1 = mapper.tableCodeExist(id);
        if (s == null && s1 != null) {
            return 0;
        } else if (s != null&&s1 != null) {
            return 2;
        } else {
            return 1;
        }
    }
}
