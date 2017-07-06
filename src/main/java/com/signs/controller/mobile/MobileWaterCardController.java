package com.signs.controller.mobile;


import com.alibaba.fastjson.JSONObject;
import com.signs.model.commons.Result;
import com.signs.model.waterCard.WaterCard;
import com.signs.service.waterCard.WaterCardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/app/card")
public class MobileWaterCardController {

    @Resource
    private WaterCardService service;

    @GetMapping("list")
    public Result list(HttpSession session){

        Result result = new Result();

        try{

            String id = session.getAttribute("id").toString();
            JSONObject object  = new JSONObject();

            object.put("gy",service.getCardByUser(id,1));
            object.put("sy",service.getCardByUser(id,2));

            result.setData(object);
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @PostMapping("setDefault")
    public Result setDefault(String cardId,HttpSession session){

        Result result = new Result();

        try{

            String id = session.getAttribute("id").toString();
            if(service.setDefault(id,cardId)){
                result.setResult(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @PostMapping("bind")
    public Result bind(HttpSession session,String code,String password,String remark){

        Result result = new Result();

        try{
            String id = session.getAttribute("id").toString();
            WaterCard waterCard = new WaterCard();
            waterCard.setPassword(password);
            waterCard.setCode(code);
            if(service.bindCard(id,waterCard,remark)){
                result.setResult(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
    @PostMapping("update")
    public Result update(String cardId,String remark){

        Result result = new Result();

        try{
            WaterCard waterCard = new WaterCard();
            waterCard.setId(cardId);
            waterCard.setRemark(remark);
            if(service.update(waterCard)){
                result.setResult(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @PostMapping("lost")
    public Result lost(String cardId){

        Result result = new Result();

        try{

            WaterCard waterCard = new WaterCard();
            waterCard.setId(cardId);
            waterCard.setStatus(3);
            if (service.update(waterCard))
                result.setResult(0);
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @PostMapping("cancelLost")
    public Result cancelLost(String cardId){

        Result result = new Result();

        try{

            WaterCard waterCard = new WaterCard();
            waterCard.setId(cardId);
            waterCard.setStatus(2);
            if (service.update(waterCard))
                result.setResult(0);
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @PostMapping("changeCard")
    public Result changeCard(String cardId,String newCardCode,String newPassword){

        Result result = new Result();

        try{

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @GetMapping("bbb")
    public Result bbb(HttpSession session){

        Result result = new Result();

        try{

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
