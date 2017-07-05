package com.signs.service.msg;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.signs.mapper.msg.MsgMapper;
import com.signs.model.commons.SmsSendRequest;
import com.signs.model.commons.SmsSendResponse;
import com.signs.model.msg.Msg;
import com.signs.util.ChuangLanSmsUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class MsgService {

    @Resource
    private MsgMapper mapper;

    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAInCL7dqV5iagQ";
    static final String accessKeySecret = "OrOuS7ACV86smR09fXTSee648QZ3GV";

    /**
     * 发送短信
     * @param phone
     * @return
     */
    public boolean sendMsg(String phone){

        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        int time = 5;

        try {

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            Msg msg = new Msg();
            msg.setPhone(phone);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(phone);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName("直饮水管理平台");
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode("SMS_75815163");

            Integer capital = (int)(Math.random()*1000000);
            msg.setCapital(capital.toString());
            Date now = new Date();
            msg.setCtime(now);
            msg.setEndTime(new Date(now.getTime()+time*60000));

            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"code\":\""+capital.toString()+"\", \"time\":\""+time+"\"}");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            //request.setOutId("yourOutId");

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            mapper.insert(msg);

            System.out.println("短信接口返回的数据----------------");
            System.out.println("Code=" + sendSmsResponse.getCode());
            System.out.println("Message=" + sendSmsResponse.getMessage());
            System.out.println("RequestId=" + sendSmsResponse.getRequestId());
            System.out.println("BizId=" + sendSmsResponse.getBizId());
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 短信验证
     * @param phone
     * @param capital
     * @return
     */
    public boolean verifyMsg(String phone,String capital){

        Msg msg = new Msg();
        msg.setPhone(phone);
        msg.setCapital(capital);
        Msg result = mapper.verify(msg);
        if(result == null){
            return false;
        }else{
            result.setEndTime(new Date());
            mapper.updateByPrimaryKeySelective(result);
            return true;
        }
    }
}
