package com.signs.controller.mobile;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.signs.service.alipay.AlipayService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/app/alipay")
public class AlipayController {

    @Resource
    private AlipayService service;

    String ALIPAY_PUBLIC_KEY ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvYwHeC6ztIGLZ5yjb0Ny7XCft+lcfSDP7ObWGowStyi5U9a4ya+4rGz2POHuXFZmvAat8G5xjM3rI4XGyqCUmi/dFTNu83RcHev8bo6WTMuhGBIHiwR7xBjMX60hIUPd8zozT2OZvAuSyKLgo2C75+nxh8yG9Gii37i6asoC2W7HiG1t/jVnNl4TRd1HYolHfS5DKtn4HhLI3w0+m77AXrNUTgy6TjbfM3BSJDj+xmMbwxYAXxpU2tTBi/VRtMTI1HQF1n08KVe+yY4hiG8x3WtQR8Kfup12qHxosrrIjz2Fc3f4sgxZvqUBtntkJK04qhGnhW2JcHkMPf9vLfRNeQIDAQAB";

    @PostMapping("send")
    public void send(){
        service.getOrder();
    }


    @PostMapping("return")
    public void back(HttpServletRequest request) throws AlipayApiException {

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
            System.out.println(name+"----"+valueStr);
            params.put(name, valueStr);
        }

        boolean flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, "UTF-8", "RSA2");
        System.out.println(flag);
    }
}
