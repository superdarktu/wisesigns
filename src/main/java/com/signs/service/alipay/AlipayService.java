package com.signs.service.alipay;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.signs.mapper.user.UserMapper;
import com.signs.mapper.userPurchaseRecord.UserPurchaseRecordMapper;
import com.signs.mapper.userRechargeRecord.UserRechargeRecordMapper;
import com.signs.mapper.waterCard.WaterCardMapper;
import com.signs.model.user.User;
import com.signs.model.userRechargeRecord.UserRechargeRecord;
import com.signs.model.waterCard.WaterCard;
import com.signs.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AlipayService {

    @Resource
    private UserRechargeRecordMapper mapper;

    @Resource
    private WaterCardMapper waterCardMapper;

    private AlipayClient alipayClient;
    String APP_ID = "2017071307735680";
    //String APP_ID = "2016080500170502";
    String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCEnetaRhT74cS3Uz9dfTb16w0Kv9wge1MTSNENx3n6qnIgd8pVUzigLRY2/AjWe1z3L14NFdBir3HQFR1ZAonQgjUidpG4ZCB3Wr313vnQlMXfQPbshbQCN+2MesJh2U7C3VMxiODW2HsbCR2+6zEwL0HxxKanbJ4pxSxsamQQVGcIYXFTxIPP1BIh3jdsbnIA/P2wt1kE0lWi++k3IX/pDq5zmbZQxgpWX7SvXejWaKoIVbjL4WRUnVmKGm/M2ipvwhY9F1VXaosSEZuYCHQR89AwPNlqMXSUa1TMhH1B1WAx8ZZZNcNa2apuhWVEwABurM0V6vLQNcrXd/5RbK3HAgMBAAECggEAfabfCu6iDTIR4B05SYxNWd42wjudBw2yXfA9W4/oLC/46Hh3tjaezW1Kwhg1C3thTzeLJlzHz+kprh2DbAWyrGPikl/Dm/EKrevFVsPI8O81OF3mV54rO3rgUZ0yteFe9dUccS+e0RHWOnx9uZpARcZQ9hD8Ul/gz/TpLWP0oV7pGqVi8yuelgDYotY7Tjf1O4m3P0Kb1vE132u/YIol53ohg3NJCKQMayfZrKcc5hLbjTjc7BX6ZfNXinw4Y5laxhByH8oMD7/DMZt0NKPlwc2WXbHjY0wAiQJvO2QAFUyt4MryR3zAFlaJjdRVgdIQn0aX/GY0HcYdG/ZRriTw0QKBgQDGCWIAM05mXdt3Cm8UuB2yZ5c8+09BO015J2G0kf2vIKOzUpQld1fP5Sm4H+uEpDo7TG17d81Vv83tpkTAfCXigsO94Jj1/q5Cjj6ZHzy2ilP5At4CvXpaZT2gyuC+vwDFt9MtIJCJHM7RBlW1QwU+AHLET6St/bmPpgkb51fVqQKBgQCrbrisTD8T6TGy3DyR41RsjaXfzm2PKYltBcoCZXAK8H+wfSxqkfq+F5DB0E5n83e3IfeobbSfUmYDktlc4NCWAysw98EfutcgYgYIfIwPK0UfpSi2CujQjoIZJ2DEyvdztpulfU0GFeSO01cZa3mNhKt8d1cXbVVhFaxSlmGt7wKBgQCrddg54DITA1ROvuPqBnb/zhztmZDHI7cyHFzF4FjKVfImAmtt/OQ0hW8XkdtyXlZknQl1iwZnJcf/6KNCHLsYYKtlLJ6C7dyml02veZIxYZbO1CKwMKlMtrcjDkSEIg9jIINgYP+DF0QhSeDPLZXOVskHGwWz9yfALbtZQ3WS8QKBgAtS5/3xl2UQTUkcRR/53TTCcoeDEjeXrc02ya441xrR4g1dZ1aux9pQoi47PUKekXTWWI6WmIzBm5120MbSztRp5yt34hjfdaBc+TsMRxAlco5xkWvc8p38G9nfgQPeyFOrZ0KaMUFKputMSH+UWtYfQliZZXZVsYMVNGnMJy2fAoGAaSRC9Z0QAOmCbPt+/8JuPlX7jhSbuBZe2v32oQ9pZ5cmzUTz7zLWNKa5GBY0r2Rn+SBZrAJBlkc6+6x+ihcDCPyIztBtCNWE7eRYHmPSqlFWYQTizea2wn4F/q6wnCLFBVwmeYmO6imAxmPjPb8kJ2OLZkJ8dEh38wgfE3xZisI=";
    String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlMnCeEmxfuHuQSkE2mvUHpAwM2zP1h/AsHLnDaWQcPBTHyPc6JUmEo4wqE9i56eDlW2yvaDsW6C/ywtPw5GVcWOwIC8dsYAhT14RuUnKC24B0AFkFgYn1RDZuIxK84e3690R675FrKwmlQaad970op1KUr7C1oNTIFC08oq3BqbfgqmciD+Ql1Dpt5NfXXr+yrdFDuqtHVSZGyEK3Sv8iVz6lNl2OwJKO3lSgjwdWKH+TtPrWg/ad4ly54dp1cmyDWxFmhxfDjeDDX4kaEE8mUoCjNKD57lxQt3q+3WVtpv32FN7WTPpFX7ucnWZK6HgnUlpcX04NQcTANRhTsqyjwIDAQAB";
    //String ALIPAY_PUBLIC_KEY ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvYwHeC6ztIGLZ5yjb0Ny7XCft+lcfSDP7ObWGowStyi5U9a4ya+4rGz2POHuXFZmvAat8G5xjM3rI4XGyqCUmi/dFTNu83RcHev8bo6WTMuhGBIHiwR7xBjMX60hIUPd8zozT2OZvAuSyKLgo2C75+nxh8yG9Gii37i6asoC2W7HiG1t/jVnNl4TRd1HYolHfS5DKtn4HhLI3w0+m77AXrNUTgy6TjbfM3BSJDj+xmMbwxYAXxpU2tTBi/VRtMTI1HQF1n08KVe+yY4hiG8x3WtQR8Kfup12qHxosrrIjz2Fc3f4sgxZvqUBtntkJK04qhGnhW2JcHkMPf9vLfRNeQIDAQAB";

    public String getOrder(Integer money,String cardNo,String userId) throws UnsupportedEncodingException {

        if(alipayClient == null){
            alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", "UTF-8", ALIPAY_PUBLIC_KEY, "RSA2");
        }
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("我是测试数据");
        model.setSubject("App支付测试Java");
        model.setOutTradeNo((new Date()).getTime() + "" + (int) (Math.random() * 1000000));
        model.setTimeoutExpress("30m");
        model.setTotalAmount("0.01");
        String backParams = money+"&"+cardNo+"&"+userId;
        model.setPassbackParams(URLEncoder.encode(backParams,"UTF-8"));
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl("http://116.62.138.55:12300/app/alipay/return");
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Transactional
    public void createRecharge(Float price, String cardNo, String id, String orderId) {

        UserRechargeRecord temp = new UserRechargeRecord();
        temp.setOrderId(orderId);
        if(mapper.selectOne(temp) != null){
            return;
        }

        UserRechargeRecord userRechargeRecord = new UserRechargeRecord();
        userRechargeRecord.setId(java.util.UUID.randomUUID().toString().replace("-", ""));
        userRechargeRecord.setCtime(new Date());
        userRechargeRecord.setPrice(price);
        userRechargeRecord.setUserId(id);
        userRechargeRecord.setCardId(cardNo);
        userRechargeRecord.setOrderId(orderId);
        Float balance = waterCardMapper.selectCode(cardNo).get(0).getBalance();
        userRechargeRecord.setBalance(balance);
        mapper.insertSelective(userRechargeRecord);
        HashMap map=new HashMap();
        map.put("cardNo",cardNo);
        map.put("price",price);
        waterCardMapper.updateMoney(map);

    }
}
