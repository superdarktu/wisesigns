package com.signs.controller.Accept;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.signs.delay.model.Contro;
import com.signs.mapper.managerUser.ManagerUserMapper;
import com.signs.model.user.User;
import com.signs.model.watermeter.Watermeter;
import com.signs.service.dataCollectors.DataCollectorsService;
import com.signs.service.dataDrinkwater.DataDrinkwaterService;
import com.signs.service.dataTotalmeter.DataTotalmeterService;
import com.signs.service.dataWatermeter.DataWatermeterService;
import com.signs.service.user.UserService;
import com.signs.service.watermeter.WatermeterService;
import com.signs.util.DelayManager;
import com.signs.util.HttpClientHelper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    private ManagerUserMapper managerUserMapper;

    public static void main(String args[]){

    }


    @RequestMapping("/interface")
    @Transactional
    public void accept(@RequestBody String json,HttpServletRequest request) throws IOException {
        try {
            String aaa = new String(json.getBytes("ISO-8859-1"), "UTF-8");
            System.out.println(aaa);
            JSONObject object = JSON.parseObject(aaa);

            if (object.get("数据类型").equals("直饮水卡数据")) {

                String cardNo = object.get("卡编号").toString();
                String watermeterCode = object.get("水表编号").toString();

                if(StringUtil.isEmpty(cardNo) || StringUtil.isEmpty(watermeterCode))
                    return;

                if(redis.boundValueOps(watermeterCode+"block").get() != null) return;


                Watermeter watermeter = watermeterService.queryByCode(watermeterCode);
                User user = userService.queryByCard(cardNo);
                if (watermeter == null || user == null) return;

                if(watermeter.getTapStatus() == 1){

                    if(user.getPrice() <= 0.0) return;
                    HttpClientHelper.open(watermeter.getCollectorCode(),watermeterCode);

                    redis.boundValueOps(cardNo).set(watermeter.getFlowTotal()+"");
                    redis.boundValueOps(watermeterCode+"block").set("1");
                    redis.boundValueOps(watermeterCode).set(user.getId());
                    Map<String,Object> map = new HashMap<>();
                    map.put("type","readingDirectWaterMeter");
                    map.put("DTUID",watermeter.getCollectorCode());
                    map.put("MeterID",watermeterCode);
                    HttpClientHelper.sendGet("http://139.196.52.84:2001/control",map,"utf-8");
                    watermeterService.changeTap(watermeter.getId());
                    Contro contro = new Contro(1,cardNo,watermeter.getCollectorCode(),watermeterCode,120000);
                    Contro controBlock = new Contro(2,watermeterCode,20000);
                    delayManager.addTask(contro);
                    delayManager.addTask(controBlock);


                }else if(watermeter.getTapStatus() ==0){

                    String str = redis.boundValueOps(cardNo).get();
                    if(str == null) return;
                    HttpClientHelper.close(watermeter.getCollectorCode(),watermeterCode);
                    redis.boundValueOps(watermeterCode+"block").set("2");
                    Map<String,Object> map = new HashMap<>();
                    map.put("type","readingDirectWaterMeter");
                    map.put("DTUID",watermeter.getCollectorCode());
                    map.put("MeterID",watermeterCode);
                    HttpClientHelper.sendGet("http://139.196.52.84:2001/control",map,"utf-8");
                    Contro contro = new Contro(2,watermeterCode,20000);
                    delayManager.addTask(contro);
                    redis.delete(str);
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

                if(StringUtil.isEmpty(watermeterCode)) return;
                String userId =  redis.boundValueOps(watermeterCode).get();
                if(StringUtil.isEmpty(userId)) return;
                String type = redis.boundValueOps(watermeterCode+"block").get();
                if(StringUtil.isEmpty(type)) return;
                if(type.equals("1")){
                    redis.boundValueOps(watermeterCode+"flow").set(object.get("累计流量").toString());
                }else if(type.equals("2")){

                    String flow1 = redis.boundValueOps(watermeterCode+"flow").get();
                    String flow2 = object.get("累计流量").toString();

                    Watermeter watermeter = watermeterService.queryByCode(watermeterCode);
                    watermeter.setFlowTotal(Float.valueOf(flow2));
                    watermeter.setFlowDay(watermeter.getFlowDay()+Float.valueOf(flow2));
                    watermeter.setFlowMonth(watermeter.getFlowMonth()+Float.valueOf(flow2));
                    watermeterService.update(watermeter);

                    Float flow = Float.valueOf(flow2) - Float.valueOf(flow1);
                    Float price = managerUserMapper.selectPrice(watermeterCode).getWaterPrice();

                }
            } else if (object.get("数据类型").equals("直饮水卡数据")) {

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/test")
    public void aaa(){

    }
}
