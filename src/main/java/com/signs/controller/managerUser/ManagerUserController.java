package com.signs.controller.managerUser;

import com.github.pagehelper.util.StringUtil;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.model.managerUser.ManagerUser;
import com.signs.service.managerUser.ManagerUserService;
import com.signs.service.waterCard.WaterCardService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/manageUser")
public class ManagerUserController {

    @Resource
    private ManagerUserService service;

    /**
     * 添加水卡
     */

    @PostMapping("/addUser")
    public Result addUser(String account, String password, String userName, Integer userType, String tel, Float prime, Float divide, Float price) {

        Result dto = new Result();
        try {
            boolean b = service.createCard(account, password, userName, userType, tel, prime, divide, price);
            String content = b ? "0" : "1";
            dto.setData(content);
        } catch (Exception ex) {
            ex.printStackTrace();
            dto.setData("1");
        }
        return dto;
    }

    /**
     * 根据传过来的一串id删除
     */
    @PostMapping("/deleteUser")
    public Result deleteUser(String id) {
        Result dto = new Result();
        try {
            service.delete(id);
            dto.setData("0");
        } catch (Exception e) {
            e.printStackTrace();
            dto.setData("1");
        }
        return dto;
    }

    /**
     * 修改用户前获取信息
     */
    @PostMapping("/gainUser")
    public Result gain(String id) {
        Result dto = new Result();
        try {
            dto.setData(service.gain(id));
        } catch (Exception e) {
            dto.setData("1");
        }
        return dto;
    }


    /**
     * 修改管理用户
     */
    @PostMapping("/reviseUser")
    public String updateUser(String id, String newAccount, String newPassword, String newUserName, Integer newUserType, String newTel, Float newPrime, Float newDivide, Float newPrice) {
        try {
            if (StringUtil.isEmpty(id)) {
                return "2";
            }
            ManagerUser save = service.save(id, newAccount, newPassword, newUserName, newUserType, newTel, newPrime, newDivide, newPrice);
            if (save == null) {
                return "1";
            }
        } catch (Exception e) {
            return "1";
        }
        return "0";
    }

    @PostMapping("/svnStatus")
    public Object pageUser(PageParam param, String type, String status, String value) {
        try {
            return service.page(param, type, status, value);
        } catch (Exception e) {
            e.printStackTrace();
            return "1";
        }
    }
}
