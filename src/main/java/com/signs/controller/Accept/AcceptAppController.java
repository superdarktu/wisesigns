package com.signs.controller.Accept;

import com.signs.delay.model.Contro;
import com.signs.mapper.managerUser.ManagerUserMapper;
import com.signs.mapper.waterFountains.WaterFountainsMapper;
import com.signs.model.commons.Result;
import com.signs.model.user.User;
import com.signs.model.waterCard.WaterCard;
import com.signs.model.waterFountains.WaterFountains;
import com.signs.model.watermeter.Watermeter;
import com.signs.service.user.UserService;
import com.signs.service.waterCard.WaterCardService;
import com.signs.service.watermeter.WatermeterService;
import com.signs.util.DelayManager;
import com.signs.util.HttpClientHelper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sao")
public class AcceptAppController {


    @Resource
    private DelayManager delayManager;

    @Resource
    private WatermeterService watermeterService;

    @Resource
    private UserService userService;
    @Resource
    private WaterCardService waterCardService;

    @Resource
    private StringRedisTemplate redis;

    @Resource
    private WaterFountainsMapper mapper;


    @RequestMapping("/first")
    public Result first(String watermeterCode) {
        Result result = new Result();
        try {
            WaterFountains waterFountains = mapper.selectWFAndTap(watermeterCode);//饮水机
            if (waterFountains == null) {
                result.setResult(1);
                result.setMsg("no waterFountains");
            } else {
                result.setData(waterFountains);
                result.setResult(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
            result.setMsg("throw Exception");
        }
        return result;

    }

    /**
     * 绑定水卡和水表
     *
     * @param watermeterCode
     * @param cardNo
     * @return
     */
    @RequestMapping("/bind")
    public Result bind(String watermeterCode, String cardNo) {
        Result result = new Result();
        try {
            if (waterCardService.bind(watermeterCode, cardNo)) {
                result.setResult(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
            result.setMsg("throw Exception");
        }
        return result;

    }


    @RequestMapping("/open")
    public Result open(String watermeterCode, String cardNo, HttpSession session) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        StringBuilder orderId = new StringBuilder();
        orderId.append(simpleDateFormat.format(new Date()));
        int i = 0;
        while (i < 1000) {
            i = (int) (Math.random() * 10000);
        }
        orderId.append(i);
        redis.boundValueOps(watermeterCode + "orderId").set(orderId.toString());
//        redis.boundValueOps(watermeterCode + "cardNo").set(cardNo);
        Result result = new Result();
        try {
            String id = session.getAttribute("id").toString();
            if (StringUtil.isEmpty(id) || StringUtil.isEmpty(watermeterCode)) {
                result.setMsg("id is null");
                return result;
            }

            if (redis.boundValueOps(watermeterCode + "block").get() != null) {
                result.setMsg("water meter is blocked");
//                redis.delete(watermeterCode+"appBlock");

                return result;
            }
//            确认数据库存在卡和表
            Watermeter watermeter = watermeterService.queryByCode(watermeterCode);
            User user = userService.queryByCard(cardNo);
            if (watermeter == null || user == null) {
                result.setMsg("mysql don't exists this user ");
                result.setResult(1);
                return result;
            }

            redis.boundValueOps(watermeterCode + "user").set(cardNo);//卡号
            redis.boundValueOps(watermeterCode + "block").set("1");//1代表锁定
//            redis.boundValueOps(watermeterCode + "app").set(user.getId());
            HttpClientHelper.open(watermeter.getCollectorCode(), watermeterCode);
            Map<String, Object> map = new HashMap<>();
            map.put("type", "readingDirectWaterMeter");
            map.put("DTUID", watermeter.getCollectorCode());
            map.put("MeterID", watermeterCode);
            HttpClientHelper.sendGet("http://139.196.52.84:2001/control", map, "utf-8");
            watermeterService.changeTap(watermeter.getId());
            Contro contro = new Contro(1, id, watermeter.getCollectorCode(), watermeterCode, 120000);
            Contro controBlock = new Contro(3, watermeterCode, 20000);
            delayManager.addTask(contro);
            delayManager.addTask(controBlock);
            result.setResult(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
            result.setMsg("throw Exception");
        }
        return result;
    }

    @RequestMapping("/close")
    public Result close(String watermeterCode, HttpSession session) {
        Result result = new Result();
        try {
            String id = session.getAttribute("id").toString();
//            String id="4";
            Watermeter watermeter = watermeterService.queryByCode(watermeterCode);
            String str = redis.boundValueOps(watermeterCode + "user").get();
            if (StringUtil.isEmpty(str) || !str.equals(id)) {
                result.setMsg("redis don't exists this user ");
                result.setResult(1);
                return result;
            }
            HttpClientHelper.close(watermeter.getCollectorCode(), watermeterCode);
            redis.boundValueOps(watermeterCode + "block").set("2");
            HttpClientHelper.close(watermeter.getCollectorCode(), watermeterCode);

            Map<String, Object> map = new HashMap<>();
            map.put("type", "readingDirectWaterMeter");
            map.put("DTUID", watermeter.getCollectorCode());
            map.put("MeterID", watermeterCode);
            HttpClientHelper.sendGet("http://139.196.52.84:2001/control", map, "utf-8");
            Contro contro = new Contro(3, watermeterCode, 20000);
            delayManager.addTask(contro);

            redis.delete(watermeterCode + "user");
            watermeterService.changeTap(watermeter.getId());
            result.setResult(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
            result.setMsg("throw Exception");
        }
        return result;
    }

}
