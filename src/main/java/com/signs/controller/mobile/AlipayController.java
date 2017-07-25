package com.signs.controller.mobile;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.signs.model.commons.Result;
import com.signs.model.waterCard.WaterCard;
import com.signs.service.alipay.AlipayService;
import com.signs.service.waterCard.WaterCardService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.nio.cs.UnicodeEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/app/alipay")
public class AlipayController {

    @Resource
    private AlipayService service;

    @Resource
    private WaterCardService waterCardService;

   // String ALIPAY_PUBLIC_KEY ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvYwHeC6ztIGLZ5yjb0Ny7XCft+lcfSDP7ObWGowStyi5U9a4ya+4rGz2POHuXFZmvAat8G5xjM3rI4XGyqCUmi/dFTNu83RcHev8bo6WTMuhGBIHiwR7xBjMX60hIUPd8zozT2OZvAuSyKLgo2C75+nxh8yG9Gii37i6asoC2W7HiG1t/jVnNl4TRd1HYolHfS5DKtn4HhLI3w0+m77AXrNUTgy6TjbfM3BSJDj+xmMbwxYAXxpU2tTBi/VRtMTI1HQF1n08KVe+yY4hiG8x3WtQR8Kfup12qHxosrrIjz2Fc3f4sgxZvqUBtntkJK04qhGnhW2JcHkMPf9vLfRNeQIDAQAB";
    String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlMnCeEmxfuHuQSkE2mvUHpAwM2zP1h/AsHLnDaWQcPBTHyPc6JUmEo4wqE9i56eDlW2yvaDsW6C/ywtPw5GVcWOwIC8dsYAhT14RuUnKC24B0AFkFgYn1RDZuIxK84e3690R675FrKwmlQaad970op1KUr7C1oNTIFC08oq3BqbfgqmciD+Ql1Dpt5NfXXr+yrdFDuqtHVSZGyEK3Sv8iVz6lNl2OwJKO3lSgjwdWKH+TtPrWg/ad4ly54dp1cmyDWxFmhxfDjeDDX4kaEE8mUoCjNKD57lxQt3q+3WVtpv32FN7WTPpFX7ucnWZK6HgnUlpcX04NQcTANRhTsqyjwIDAQAB";
    @PostMapping("send")
    public Result send(Integer money, String cradNo, HttpSession session){
        Result result = new Result();
        try{
            if(!waterCardService.selectCode(cradNo)){
                result.setResult(1);
                return result;
            }
            String id = session.getAttribute("id").toString();
            result.setData(service.getOrder(money,cradNo,id));
        }catch (Exception e){
            result.setResult(1);
        }
        return result;
    }


    @PostMapping("return")
    public void back(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {

        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            System.out.println(name+"------"+valueStr);
            params.put(name, valueStr);
        }

        boolean flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, "UTF-8", "RSA2");
        if(flag){

            String[] parms = URLDecoder.decode(params.get("passback_params"),"UTF-8").split("&");
            String orderId = params.get("trade_no");
            service.createRecharge(Float.valueOf(parms[0]),parms[1],parms[2],orderId);
        }
    }

    /**
     * 返回值
     */
    @PostMapping("back")
    public Result back(String cardNo){
        Result result = new Result();
        try{
            result.setData(waterCardService.query(cardNo));
        }catch (Exception e){
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }
}
