package com.signs.controller.mobile;


import com.alibaba.fastjson.JSONObject;
import com.signs.model.commons.Result;
import com.signs.model.user.User;
import com.signs.model.waterCard.WaterCard;
import com.signs.service.msg.MsgService;
import com.signs.service.user.UserService;
import com.signs.service.userPurchaseRecord.UserPurchaseRecordService;
import com.signs.service.userRechargeRecord.UserRechargeRecordService;
import com.signs.service.waterCard.WaterCardService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@RequestMapping("/app/user")
public class MobileUserController {

    @Resource
    private UserService service;

    @Resource
    private MsgService msgService;

    @Resource
    private UserRechargeRecordService userRechargeRecordService;

    @Resource
    private UserPurchaseRecordService userPurchaseRecordService;

    @Resource
    private WaterCardService waterCardService;


    /**
     * 登录
     *
     * @param phone
     * @param capital
     * @param session
     * @return
     */
    @GetMapping("/login")
    public Result login(String phone, String capital, HttpSession session) {

        Result result = new Result();
        try {
            if (msgService.verifyMsg(phone, capital)) {
                User user = service.queryByPhone(phone);
                if (user == null) {
                    result.setResult(2);
                } else {
                    result.setResult(0);
                    result.setData(user);
                    session.setMaxInactiveInterval(86400*30);
                    session.setAttribute("id", user.getId());
                    session.setAttribute("type", 5);
                }
            } else {
                result.setResult(1);
                result.setMsg("验证码不正确");
            }
        } catch (Exception e) {
            result.setResult(1);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/msg")
    public Result msg(String phone) {
        Result result = new Result();
        try{
            msgService.sendMsg(phone);
            result.setResult(0);
        }catch (Exception e){
            e.printStackTrace();
            result.setResult(1);
        }

        return result;
    }

    /**
     * 注册
     *
     * @param phone
     * @param name
     * @param img
     * @return
     */
    @PostMapping("/reg")
    public Result reg(String phone, String name, String img, HttpSession session) {

        Result result = new Result();
        try {
            User user = new User();
            user.setPhone(phone);
            user.setName(name);
            user.setImg(img);
            user.setCtime(new Date());
            user.setId(java.util.UUID.randomUUID().toString().replace("-", ""));
            service.create(user);
            session.setMaxInactiveInterval(86400*30);
            session.setAttribute("id", user.getId());
            session.setAttribute("type", 5);
            result.setData(user);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    /**
     * 查询用户信息
     *
     * @return
     */
    @GetMapping
    public Result query(HttpSession session) {
        Result result = new Result();
        try {
            String userId = session.getAttribute("id").toString();
            User user = service.queryById(userId);
            String cardNo = waterCardService.selectDefaultCardNo(userId);
            if (cardNo != null) {
                WaterCard waterCard = waterCardService.query(cardNo);
                if (waterCard != null) {
                    user.setCardNo(waterCard.getCode());
                    user.setPrice(waterCard.getBalance());
                }
            }

            result.setData(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 主页
     *
     * @param session
     * @return
     */
    @GetMapping("/main")
    public Result main(HttpSession session) {
        Result result = new Result();
        try {
            String userId = session.getAttribute("id").toString();
            String cardNo = waterCardService.selectDefaultCardNo(userId);
            if (cardNo == null) {
                result.setResult(9);
                return result;
            }
            JSONObject object = new JSONObject();
            JSONObject month = new JSONObject();
            object.put("cz", userRechargeRecordService.getLast(userId));
            object.put("xf", userPurchaseRecordService.selectDay(userId));
            month.put("cz", userRechargeRecordService.getMonthPrice(cardNo));
            month.put("xf", userPurchaseRecordService.selectMonth(cardNo));
            month.put("cardNo",cardNo);
            month.put("nowMonth",new Date().getMonth()+1);
            object.put("month", month);

            result.setData(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(new Date().getMonth());
    }
}
