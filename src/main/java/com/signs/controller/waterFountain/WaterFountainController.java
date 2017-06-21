package com.signs.controller.waterFountain;

import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.model.waterFountains.WaterFountains;
import com.signs.service.waterFountains.WaterFountainsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/water")
public class WaterFountainController {

    @Resource
    private WaterFountainsService service;

    /**
     * 添加饮水机
     */

    @PostMapping("/addDispenser")
    public Result addDispenser(String waterNumber, String waterPosition, String tableNumber, Integer waterType, Float longitude, Float latitude) {

        Result dto = new Result();
        try {
            boolean b = service.createFountains(waterNumber, waterPosition, tableNumber, waterType, longitude, latitude);
            String content = b ? "0" : "1";
            dto.setData(content);
        } catch (Exception ex) {
            ex.printStackTrace();
            dto.setData("1");
        }
        return dto;
    }

    /**
     * 修改用户前获取信息
     *
     */
    @PostMapping("/gainDispenser")
    public Result gain(String id) {
        Result dto = new Result();
        try {
            dto.setData(service.gain(id));
        } catch (Exception e) {
            dto.setData("1");
        }
        return dto;
    }


    /**
     * 修改饮水机
     *
     */
    @PostMapping("/reviseDispenser")
    public String updateUser(String id, String newWaterNumber, String newWaterPosition, String newTableNumber, Integer newWaterType, Float newLongitude, Float newLatitude) {
        try {
            if (StringUtil.isEmpty(id)) {
                return "2";
            }
            WaterFountains save = service.save(id, newWaterNumber, newWaterPosition, newTableNumber, newWaterType, newLongitude, newLatitude);
            if (save == null) {
                return "1";
            }
        } catch (Exception e) {
            return "1";
        }
        return "0";
    }

    /**
     * 根据传过来的一串id删除
     *
     */
    @PostMapping("/deleteWater")
    public Result deleteUser(String id) {
        Result dto = new Result();
        try {
            service.delete(id);
            dto.setData("0");
        } catch (Exception e) {
            e.printStackTrace();
            dto.setData("1");
        }
        return dto;
    }

    @PostMapping("/queryType")
    public Object pageUser(PageParam param, String type, String value) {
        try {
         return service.page(param, type, value);
        } catch (Exception e) {
            e.printStackTrace();
           return "1";
        }
    }
}
