package com.signs.controller.Accept;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.signs.delay.model.Contro;
import com.signs.mapper.managerUser.ManagerUserMapper;
import com.signs.mapper.userPurchaseRecord.UserPurchaseRecordMapper;
import com.signs.model.user.User;
import com.signs.model.userPurchaseRecord.UserPurchaseRecord;
import com.signs.model.waterCard.WaterCard;
import com.signs.model.watermeter.Watermeter;
import com.signs.service.dataCollectors.DataCollectorsService;
import com.signs.service.dataDrinkwater.DataDrinkwaterService;
import com.signs.service.dataTotalmeter.DataTotalmeterService;
import com.signs.service.dataWatermeter.DataWatermeterService;
import com.signs.service.user.UserService;
import com.signs.service.userPurchaseRecord.UserPurchaseRecordService;
import com.signs.service.waterCard.WaterCardService;
import com.signs.service.waterFountains.WaterFountainsService;
import com.signs.service.watermeter.WatermeterService;
import com.signs.util.DelayManager;
import com.signs.util.HttpClientHelper;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AcceptController {

    @Resource
    private DataCollectorsService dataCollectorsService;

    @Resource
    private DataDrinkwaterService dataDrinkwaterService;

    @Resource
    private DataWatermeterService dataWatermeterService;

    @Resource
    private DataTotalmeterService dataTotalmeterService;

    @Resource
    private DelayManager delayManager;

    @Resource
    private WatermeterService watermeterService;

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate redis;

    @Resource
    private UserPurchaseRecordService userPurchaseRecordService;

    @Resource
    private WaterFountainsService waterFountainsService;
    @Resource
    private WaterCardService waterCardService;

    public static void main(String args[]) {

    }


    @RequestMapping("/interface")
    @Transactional
    public void accept(@RequestBody String json, HttpServletRequest request) throws IOException {
        try {
            String aaa = new String(json.getBytes("ISO-8859-1"), "UTF-8");
            System.out.println(aaa);
            JSONObject object = JSON.parseObject(aaa);

            if (object.get("数据类型").equals("直饮水卡数据")) {

                redis.boundValueOps("app:app").set("1");
                if(true) return;
                String cardNo = object.get("卡编号").toString();
                String watermeterCode = object.get("水表编号").toString();
            //    redis.delete(watermeterCode + "block");

                if (StringUtil.isEmpty(cardNo) || StringUtil.isEmpty(watermeterCode))
                    return;
             //
                if (redis.boundValueOps(watermeterCode + "block").get() != null) return;


                Watermeter watermeter = watermeterService.queryByCode(watermeterCode);
                User user = userService.queryByCard(cardNo);
                if (watermeter == null || user == null) return;

                if (watermeter.getTapStatus() == 1) {

                    if (user.getPrice() <= 0.0) return;
                    HttpClientHelper.open(watermeter.getCollectorCode(), watermeterCode);

                    redis.boundValueOps(watermeterCode + "user").set(cardNo);
                    redis.boundValueOps(watermeterCode + "block").set("1");
                    redis.boundValueOps(watermeterCode).set(user.getId());

                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "readingDirectWaterMeter");
                    map.put("DTUID", watermeter.getCollectorCode());
                    map.put("MeterID", watermeterCode);
                    HttpClientHelper.sendGet("http://139.196.52.84:2001/control", map, "utf-8");
                    watermeterService.changeTap(watermeter.getId());
                    Contro contro = new Contro(1, cardNo, watermeter.getCollectorCode(), watermeterCode, 120000);
                    Contro controBlock = new Contro(2, watermeterCode, 20000);
                    delayManager.addTask(contro);
                    delayManager.addTask(controBlock);


                } else if (watermeter.getTapStatus() == 0) {

                    String str = redis.boundValueOps(watermeterCode + "user").get();
                    if (StringUtil.isEmpty(str) || !str.equals(cardNo)) return;
                    HttpClientHelper.close(watermeter.getCollectorCode(), watermeterCode);
                    redis.boundValueOps(watermeterCode + "block").set("2");
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "readingDirectWaterMeter");
                    map.put("DTUID", watermeter.getCollectorCode());
                    map.put("MeterID", watermeterCode);
                    HttpClientHelper.sendGet("http://139.196.52.84:2001/control", map, "utf-8");
                    Contro contro = new Contro(2, watermeterCode, 20000);
                    delayManager.addTask(contro);
                    watermeterService.changeTap(watermeter.getId());
                }

            } else if (object.get("数据类型").equals("总表定时上报数据")) {
                dataTotalmeterService.create(object, 1);
            } else if (object.get("数据类型").equals("水表定时上报数据")) {
                dataWatermeterService.create(object, 1);
            } else if (object.get("数据类型").equals("DTU在线")) {

            } else if (object.get("数据类型").equals("总表即时上报数据")) {
                dataTotalmeterService.create(object, 2);
            } else if (object.get("数据类型").equals("水表即时上报数据")) {
                dataWatermeterService.create(object, 2);
            } else if (object.get("数据类型").equals("采集器在线")) {
                dataCollectorsService.create(object);
            } else if (object.get("数据类型").equals("直饮水表数据(阀门状态:1-开,2-关,3-故障)")) {

                String watermeterCode = object.get("水表编号").toString();

                if (StringUtil.isEmpty(watermeterCode)) return;
                String userId = redis.boundValueOps(watermeterCode).get();
                if (StringUtil.isEmpty(userId)) return;
                String type = redis.boundValueOps(watermeterCode + "block").get();
                if (StringUtil.isEmpty(type)) return;
                if (type.equals("1")) {
                    redis.boundValueOps(watermeterCode + "flow").set(object.get("累计流量").toString());
                } else if (type.equals("2")) {

                    String flow1 = redis.boundValueOps(watermeterCode + "flow").get();
                    String flow2 = object.get("累计流量").toString();

                    Watermeter watermeter = watermeterService.queryByCode(watermeterCode);
                    watermeter.setFlowTotal(Float.valueOf(flow2));
                    watermeter.setFlowDay(watermeter.getFlowDay() + Float.valueOf(flow2));
                    watermeter.setFlowMonth(watermeter.getFlowMonth() + Float.valueOf(flow2));
                    watermeterService.update(watermeter);

                    Float flow = Float.valueOf(flow2) - Float.valueOf(flow1);
                    if (flow <= 0.00000000001) return;
                    Float unit_cost = waterFountainsService.getPrice(watermeterCode);
                    Float price = flow * unit_cost * 1000;
                    String cardNo = redis.boundValueOps(watermeterCode + "cardNo").get();
                    WaterCard waterCard = waterCardService.query(cardNo);
                    if (waterCard != null) {
                        Float balance = waterCard.getBalance() - price;
                        UserPurchaseRecord record = new UserPurchaseRecord();
                        record.setUnitCost(unit_cost);
                        record.setBalance(balance);
                        record.setPrice(price);
                        record.setCardNo(cardNo);
                        record.setWaterConsumption(flow);
                        record.setUserId(userId);
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//                        StringBuilder builder = new StringBuilder();
//                        builder.append(simpleDateFormat.format(new Date()));
//                        int i = 0;
//                        while (i < 1000) {
//                            i = (int) (Math.random() * 10000);
//                        }
//                        builder.append(i);
                        String orderId = redis.boundValueOps(watermeterCode + "orderId").get();
                        record.setOrderId(orderId);
                        userPurchaseRecordService.createUserPurchaseRecord(record, watermeterCode);
                    }
                    redis.delete(watermeterCode + "cardNo");
                }
            } else if (object.get("数据类型").equals("直饮水卡数据")) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

