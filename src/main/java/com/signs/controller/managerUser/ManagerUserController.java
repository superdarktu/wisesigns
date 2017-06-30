package com.signs.controller.managerUser;

import com.github.pagehelper.util.StringUtil;
import com.signs.dto.Collector.CollectorVO;
import com.signs.model.collector.Collector;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.model.commons.TwoDataResult;
import com.signs.model.managerUser.ManagerUser;
import com.signs.service.collector.CollectorService;
import com.signs.service.managerUser.ManagerUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
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
            if (userType == 1) {
                List<Collector> collectors1 = service1.findWithNoProperty();
                dto.setData(collectors1);
            } else {
                List<CollectorVO> collectors2 = service1.page(param, new Collector()).getList();
                dto.setData(collectors2);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            dto.setData("1");
        }
        return dto;
    }

    /**
     * 修改用户所需采集器
     */
    @PostMapping("/modifyUser")
    public TwoDataResult modifyUser(String id) {
        TwoDataResult twoDataResult = new TwoDataResult();
        try {
            List<Collector> notyet = service1.findWithNoProperty();
            List<String> already = service1.findByManager(id);
            if (already != null) {
                twoDataResult.setRecharge(already);
            }
            twoDataResult.setConsume(notyet);
            twoDataResult.setResult(0);
        } catch (Exception e) {
            e.printStackTrace();
            twoDataResult.setResult(1);
        }
        return twoDataResult;
    }


    /**
     * 添加用户
     */
    @PostMapping("/submit")
    public Result addUser(String account, String password, String userName, Integer userType, String tel, Float prime, Float divide, Float price, String collector) {
        Result dto = new Result();
        ManagerUser managerUser =new ManagerUser();
        managerUser.setAccount(account);
        managerUser.setPassword(password);
        managerUser.setName(userName);
        managerUser.setUserType(userType);
        managerUser.setPhone(tel);
        managerUser.setCostScale(prime);
        managerUser.setIvisionProportion(divide);
        managerUser.setWaterPrice(price);
        try {
            boolean b = service.createUser(managerUser, collector);
            int content = b ? 0 : 1;
            dto.setResult(content);
        } catch (Exception ex) {
            ex.printStackTrace();
            dto.setData("抛出异常");
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
            dto.setResult(0);
        } catch (Exception e) {
            e.printStackTrace();
            dto.setResult(1);
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
            dto.setResult(0);
        } catch (Exception e) {
            dto.setResult(1);
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
            ManagerUser managerUser = new ManagerUser();
            managerUser.setId(id);
            managerUser.setCtime(new Date());
            managerUser.setAccount(newAccount);
            managerUser.setPassword(newPassword);
            managerUser.setUserType(newUserType);
            managerUser.setName(newUserName);
            managerUser.setPhone(newTel);
            managerUser.setCostScale(newPrime);
            managerUser.setIvisionProportion(newDivide);
            managerUser.setWaterPrice(newPrice);
            ManagerUser save = service.save(managerUser, newcollector);
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
    public Result pageUser(PageParam param, String status, String value) {
        Result result = new Result();
        try {
            result.setData(service.page(param, status, value));
            result.setResult(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }

    /**
     * 查询下属采集器
     */
    @PostMapping("/branchInquiry")
    public Result branchInquiry(String id) {
        Result result = new Result();
        try {
            result.setData(service1.findByManager(id));
            result.setResult(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }


    /**
     * 采集器模糊查询
     */
    @PostMapping("/inquiry")
    public Result inquiry(PageParam param, String value) {
        Result result = new Result();
        try {
            Collector collector = new Collector();
            collector.setName(value);
            result.setData(service1.page(param, collector));
            result.setResult(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(1);
        }
        return result;
    }
}
