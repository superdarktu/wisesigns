package com.signs.service.waterFountains;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.signs.mapper.managerUser.ManagerUserMapper;
import com.signs.mapper.waterFountains.WaterFountainsMapper;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.managerUser.ManagerUser;
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
    @Resource
    private ManagerUserMapper mapper1;


    /**
     * 创饮水机
     *
     */
    @Transactional
    public boolean createFountains(String waterNumber, String waterPosition, String tableNumber, Integer waterType, Float longitude, Float latitude) {
//        卡号不重复
        List<WaterFountains> fountains = mapper.selectCode(waterNumber);
        if (fountains == null|| fountains.size()>0) return false;
        ManagerUser managerUser = mapper1.selectPrice(tableNumber);
        WaterFountains waterFountains = new WaterFountains();
        waterFountains.setId(java.util.UUID.randomUUID().toString().replace("-", ""));
        waterFountains.setCtime(new Date());
        waterFountains.setCode(waterNumber);
        waterFountains.setTableCode(tableNumber);
        waterFountains.setPlace(waterPosition);
        waterFountains.setLatitude(latitude);
        waterFountains.setLongitude(longitude);
        waterFountains.setType(waterType);
        waterFountains.setWaterPrice(managerUser.getWaterPrice());
        waterFountains.setCostScale(managerUser.getCostScale());
        mapper.insert(waterFountains);
        return true;
    }


    /**
     * 根据ids删除
     *
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
     *
     */
    public WaterFountains gain(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 修改饮水机信息
     *
     */
    public WaterFountains save(String id, String newWaterNumber, String newWaterPosition, String newTableNumber, Integer newWaterType, Float newLongitude, Float newLatitude) {
//        卡号不重复
        List<WaterFountains> fountains = mapper.selectCode(newWaterNumber);
        if (fountains == null|| fountains.size()>0) return null;
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
     *
     */
    public PageInfo<WaterFountains>
    page(PageParam page, String type, String value) {
        if (page.getPageNo() != null && page.getPageSize() != null) PageHelper.startPage(page.getPageNo(), page.getPageSize());
        HashMap<String,String> hashMap = new HashMap<>();
        if (type != null) {
            hashMap.put("type", type);
        }
        if (value != null) {
            hashMap.put("value", "%"+value+"%");
        }
        return new PageInfo(mapper.getDispenser(hashMap)) ;
    }
}
