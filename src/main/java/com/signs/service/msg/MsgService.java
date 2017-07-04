package com.signs.service.msg;

import com.alibaba.fastjson.JSON;
import com.signs.mapper.msg.MsgMapper;
import com.signs.model.commons.SmsSendRequest;
import com.signs.model.commons.SmsSendResponse;
import com.signs.util.ChuangLanSmsUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MsgService {

    @Resource
    private MsgMapper msgMapper;

    // 用户平台API账号(非登录账号,示例:N1234567)
    public static String account = "N4105432";
    // 用户平台API密码(非登录密码)
    public static String pswd = "6405139d";

    /**
     *
     * @param phone
     * @return
     */
    public boolean sendMsg(String phone){

        String smsSingleRequestServerUrl = "http://vsms.253.com/msg/send/json";
        Integer capital  = (int)(Math.random()*1000000);
        String msg = "直饮水平台：您的验证码为 "+capital+" 请妥善保存";

        SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone);

        String requestJson = JSON.toJSONString(smsSingleRequest);

        System.out.println("before request string is: " + requestJson);

        String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);

        System.out.println("response after request result is :" + response);

        SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);

        System.out.println("response  toString is :" + smsSingleResponse);
        return true;
    }
}
