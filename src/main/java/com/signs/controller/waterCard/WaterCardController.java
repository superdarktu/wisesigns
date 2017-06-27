package com.signs.controller.waterCard;

import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.service.waterCard.WaterCardService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/waterCard")
public class WaterCardController {

    @Resource
    private WaterCardService service;

    /**
     * 添加水卡
     */
    @PostMapping("/addWaterCard")
    public Result addWaterCard(String cardNumberi, String password, Integer type) {

        Result dto = new Result();
        try {
            boolean b = service.createCard(cardNumberi, password, type);
            String content=b?"0":"1";
            dto.setData(content);
        } catch (Exception ex) {
            ex.printStackTrace();
            dto.setResult(1);
        }
        return dto;
    }

    /**
     * 根据传过来的一串id删除
     */
    @PostMapping("/deleteCard")
    public Result deleteCard(String id) {
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

    /**
     * 查询水卡
     */
    @PostMapping("/queryType")
    public Result pageCard(PageParam param, String type,String status, String value) {
        Result result=new Result();
        try {
        result.setData(service.page(param,type,status,value));
        } catch (Exception e) {
            e.printStackTrace();
           result.setData("1");
        }
        return result;
    }
}
