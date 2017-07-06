package com.signs.controller.mobile;


import com.alibaba.fastjson.JSONObject;
import com.signs.model.commons.Result;
import com.signs.model.user.User;
import com.signs.service.msg.MsgService;
import com.signs.service.user.UserService;
import com.signs.service.userPurchaseRecord.UserPurchaseRecordService;
import com.signs.service.userRechargeRecord.UserRechargeRecordService;
import com.signs.service.waterCard.WaterCardService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

                    result.setData(user);
                    session.setAttribute("id", user.getId());
                    session.setAttribute("type", 5);
                }
            } else {
                result.setResult(1);
                result.setMsg("验证码不正确");
            }
            result.setResult(0);
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
    public Result reg(String phone, String name, String img) {

        Result result = new Result();
        try {
            User user = new User();
            user.setPhone(phone);
            user.setName(name);
            user.setImg(img);
            user.setCtime(new Date());
            user.setPrice(0f);
            user.setId(java.util.UUID.randomUUID().toString().replace("-", ""));
            service.create(user);
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
            user.setCardNo(waterCardService.selectDefaultCardNo(userId));
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
            JSONObject object = new JSONObject();
            JSONObject month = new JSONObject();
            object.put("cz", userRechargeRecordService.getLast(userId));
            object.put("xf", userPurchaseRecordService.selectDay(userId));
            month.put("cz", userRechargeRecordService.getMonthPrice(cardNo));
            month.put("xf", userPurchaseRecordService.selectMonth(cardNo));
            object.put("month", month);
            result.setData(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
