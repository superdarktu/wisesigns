package com.signs.controller.managerUser;

import com.github.pagehelper.util.StringUtil;
import com.signs.dto.Collector.CollectorVO;
import com.signs.model.collector.Collector;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.model.managerUser.ManagerUser;
import com.signs.service.collector.CollectorService;
import com.signs.service.managerUser.ManagerUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/manageUser")
public class ManagerUserController {

    @Resource
    private ManagerUserService service;
    @Resource
    private CollectorService service1;

    /**
     * 用户类型
     */
    @PostMapping("/addUser")
    public Result gainUser(PageParam param, Integer userType) {

        Result dto = new Result();
        try {
            if (userType == 3) {
                List<Collector> collectors1 = service1.findWithNoProperty();
                dto.setData(collectors1);
            } else {
                PageInfo<CollectorVO> collectors2 = service1.page(param, new Collector());
                dto.setData(collectors2);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            dto.setData("1");
        }
        return dto;
    }


    /**
     * 添加用户
     */
    @PostMapping("/submit")
    public Result addUser(String account, String password, String userName, Integer userType, String tel, Float prime, Float divide, Float price, String collector) {
        Result dto = new Result();
        try {
            boolean b = service.createUser(account, password, userName, userType, tel, prime, divide, price);
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
    public String updateUser(String id, String newAccount, String newPassword, String newUserName, Integer newUserType, String newTel, Float newPrime, Float newDivide, Float newPrice, String newcollector) {
        try {
            if (StringUtil.isEmpty(id)) return "2";
            ManagerUser save = service.save(id, newAccount, newPassword, newUserName, newUserType, newTel, newPrime, newDivide, newPrice);
            if (save == null) return "1";
        } catch (Exception e) {
            return "1";
        }
        return "0";
    }

    /**
     * 查询管理用户
     */
    @PostMapping("/svnStatus")
    public Object pageUser(PageParam param, String type, String status, String value) {
        try {
            return service.page(param, type, status, value);
        } catch (Exception e) {
            e.printStackTrace();
            return "1";
        }
    }

    /**
     * 查询下属采集器
     */
    @PostMapping("/branchInquiry")
    public Object branchInquiry(String id) {
        try {
            return service1.findByManager(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "1";
        }
    }


    /**
     * 采集器模糊查询
     */
    @PostMapping("/inquiry")
    public Object inquiry() {
        try {
            return "0";
        } catch (Exception e) {
            e.printStackTrace();
            return "1";
        }
    }
}
