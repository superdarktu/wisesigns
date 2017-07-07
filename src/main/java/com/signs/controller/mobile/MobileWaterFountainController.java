package com.signs.controller.mobile;

import com.signs.model.commons.Result;
import com.signs.service.waterFountains.WaterFountainsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app/water")
public class MobileWaterFountainController {

    @Resource
    private WaterFountainsService service;

    /**
     * 获取所有公共饮水机
     */
    @GetMapping("/public")
    public Result getPublic(){
        Result result = new Result();
        try {
            result.setData(service.pagePublic());
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
