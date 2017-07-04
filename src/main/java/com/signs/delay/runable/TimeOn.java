package com.signs.delay.runable;

import com.signs.delay.model.Contro;
import com.signs.model.watermeter.Watermeter;
import com.signs.service.watermeter.WatermeterService;
import com.signs.util.HttpClientHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;

@Component
public class TimeOn implements  Runnable{

    private DelayQueue<Contro> queue;

    @Autowired
    private WatermeterService watermeterService;

    @Resource
    private StringRedisTemplate redis;

    /*public TimeOn(DelayQueue<Contro> queue){
        this.queue = queue;
    }*/

    @Override
    public void run() {
        while(true){
            try {
                Contro take = queue.take();
                if(take.getType() == 1) {
                    Watermeter watermeter = watermeterService.queryByCode(take.getMemberId());
                    if (watermeter.getTapStatus() == 0) {

                        HttpClientHelper.close(take.getCollectorCode(), take.getMemberId());
                        redis.boundValueOps(take.getMemberId()+"block").set("2");
                        watermeterService.changeTap(watermeter.getId());
                        Map<String,Object> map = new HashMap<>();
                        map.put("type","readingDirectWaterMeter");
                        map.put("DTUID",watermeter.getCollectorCode());
                        map.put("MeterID",take.getMemberId());
                        HttpClientHelper.sendGet("http://139.196.52.84:2001/control",map,"utf-8");
                        String str = redis.boundValueOps(take.getCardNo()).get();
                        if (str == null) return;
                    }
                }else if(take.getType() == 2){

                    redis.delete(take.getMemberId()+"block");
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setQueue(DelayQueue<Contro> queue) {
        this.queue = queue;
    }
}
