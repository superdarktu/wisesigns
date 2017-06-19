package com.signs.controller.manager;

import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.model.manager.Manager;
import com.signs.service.manager.ManagerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Resource
    private ManagerService service;


    @PostMapping("/login")
    public Result loginIn(Manager model) {

        Result dto = new Result();
        try {
            if (!service.isHaveUsername(model.getUserName())) {
                dto.setData("2");
            } else {
                Manager manager = service.login(model.getUserName(), model.getPassword());

                if (manager == null) {
                    dto.setData("1");
                } else {
                    dto.setData(manager);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dto;
    }

    /**
     * 添加用户
     *
     * @param manager
     * @return
     */
    @PostMapping("/createUser")
    public String createUser(Manager manager) {
        try {
            if (service.isHaveUsername(manager.getUserName())) {
                return "2";
            }
            service.createUser(manager);
        } catch (Exception e) {
            return "1";
        }
        return "0";
    }

    /**
     * 修改用户
     *
     * @param manager
     * @return
     */
    @PostMapping("/updateUser")
    public String updateUser(Manager manager) {
        try {
            if (StringUtil.isEmpty(manager.getId())) {
                return "2";
            }
            service.save(manager);
        } catch (Exception e) {
            return "1";
        }
        return "0";
    }

    /**
     * 根据传过来的一串id删除
     *
     * @param idstr
     * @return
     */
    @PostMapping("/deleteUser")
    public Result deleteUser(String idstr) {
        Result dto = new Result();
        try {
            service.delete(idstr);
            dto.setData("0");
        } catch (Exception e) {
            e.printStackTrace();
            dto.setData("1");
        }
        return dto;
    }

    @PostMapping("/pageUser")
    public Result pageUser(PageParam param, String keyWord) {
        Result dto = new Result();
        try {
            dto.setData(service.page(param, keyWord));
        } catch (Exception e) {
            e.printStackTrace();
            dto.setData("1");
        }
        return dto;
    }


    /**
     * 检查用户名是否可用
     *
     * @param userName
     * @return
     */
    @PostMapping("/checkUserName")
    public Result checkUserName(String userName) {
        Result dto = new Result();
        try {
            if (service.isHaveUsername(userName)) {
                dto.setData("1");
            } else {
                dto.setData("0");
            }
        } catch (Exception e) {
            dto.setData("1");
        }
        return dto;
    }

    /**
     * 修改密码
     * @param id
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @PostMapping("/updatePassword")
    public Result updatePassword(String id, String oldPassword, String newPassword) {
        Result dto = new Result();
        dto.setData("1");
        try {
            if (service.updatePassword(id,oldPassword,newPassword)) {
                dto.setData("0");
            }
        } catch (Exception e) {
            dto.setData("1");
        }
        return dto;
    }

    /**
     * 重置密码为123456
     * @param id
     * @return
     */
    @PostMapping("/resetPassword")
    public Result resetPassword(String id) {
        Result dto = new Result();
        dto.setData("1");
        try {
            if (service.resetPassword(id)) {
                dto.setData("0");
            }
        } catch (Exception e) {
            dto.setData("1");
        }
        return dto;
    }

}