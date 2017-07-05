package com.signs.controller.mobile;


import com.signs.model.commons.Result;
import com.signs.model.user.User;
import com.signs.service.msg.MsgService;
import com.signs.service.user.UserService;
import com.signs.service.userPurchaseRecord.UserPurchaseRecordService;
import com.signs.service.userRechargeRecord.UserRechargeRecordService;
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


    /**
     * 登录
     * @param phone
     * @param capital
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result login(String phone, String capital, HttpSession session){

        Result result = new Result();
        try{
            if(msgService.verifyMsg(phone,capital)){
                User user = service.queryByPhone(phone);
                if(user == null){

                    result.setResult(2);
                }else{

                    result.setData(user);
                    session.setAttribute("id",user.getId());
                    session.setAttribute("type",5);
                }
            }else{
                result.setResult(1);
                result.setMsg("验证码不正确");
            }
        }catch (Exception e){
            result.setResult(1);
            e.printStackTrace();
        }
        return  result;
    }

    @PostMapping("/msg")
    public Result msg(String phone){

        Result result = new Result();
        msgService.sendMsg(phone);
        return  result;
    }

    /**
     * 注册
     * @param phone
     * @param name
     * @param img
     * @return
     */
    @PostMapping("/reg")
    public Result reg(String phone,String name,String img){

        Result result = new Result();
        try{

            User user = new User();
            user.setImg(img);
            user.setCtime(new Date());
            user.setPrice(new Float(0));
            user.setId(java.util.UUID.randomUUID().toString().replace("-", ""));
            service.create(user);
            result.setData(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  result;
    }

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public Result query(@PathVariable("userId") String userId){
        Result result = new Result();
        try {
            result.setData(service.queryById(userId));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
