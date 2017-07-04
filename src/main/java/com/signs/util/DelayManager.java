package com.signs.util;

import com.signs.delay.model.Contro;
import com.signs.delay.runable.TimeOn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.DelayQueue;

@Component
public class DelayManager {

    private DelayQueue<Contro> queue;

    @Autowired
    private TimeOn timeOn;

    public  void addTask(Contro contro){
        if(this.queue == null){
            this.queue = new DelayQueue<Contro>();
            timeOn.setQueue(this.queue);
            new Thread(timeOn).start();
        }
        queue.offer(contro);
    }
}
