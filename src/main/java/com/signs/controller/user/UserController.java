package com.signs.controller.user;

import com.signs.model.commons.Result;
import com.signs.model.manager.Manager;
import com.signs.model.managerUser.ManagerUser;
import com.signs.service.manager.ManagerService;
import com.signs.service.managerUser.ManagerUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private ManagerService service;

    @Resource
    private ManagerUserService managerUserService;

    /**
     * 登录
     *
     * @param model
     * @param httpSession
     * @return
     */
    @PostMapping("/login")
    public Result loginIn(Manager model, HttpSession httpSession) {

        Result dto = new Result();
        try {
            if (!service.isHaveUsername(model.getUserName())) {
                dto.setResult(2);
            } else {
                Manager manager = service.login(model.getUserName(), model.getPassword());

                if (manager == null) {
                    ManagerUser managerUser = managerUserService.login(model.getUserName(), model.getPassword());
                    if (managerUser == null) {
                        dto.setResult(1);
                    } else {
                        dto.setData(managerUser);
                        if (managerUser.getUserType() == 1) {
                            dto.setInfo("2");
                        } else {
                            dto.setInfo("3");
                        }
                        httpSession.setAttribute("id", managerUser.getId());
                        httpSession.setAttribute("type", "2");
                    }
                } else {
                    dto.setData(manager);
                    dto.setInfo("1");
                    httpSession.setAttribute("id", manager.getId());
                    httpSession.setAttribute("type", "1");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dto;
    }

    /**
     * 修改用户
     *
     * @return
     */
    @PostMapping("/revise")
    public Result updateUser(String newTel, String newName, HttpSession httpSession) {

        Result result = new Result();
        try {
            String id = httpSession.getAttribute("id").toString();
            String type = httpSession.getAttribute("type").toString();

            if (type.equals("1")) {

                Manager manager = new Manager();
                manager.setId(id);
                manager.setTel(newTel);
                manager.setName(newName);
                service.save(manager);
                result.setData(manager);
            } else {

                ManagerUser managerUser = new ManagerUser();
                managerUser.setId(id);
                managerUser.setPhone(newTel);
                managerUser.setName(newName);
                managerUserService.save(managerUser, "");
                result.setData(managerUser);
            }
        } catch (Exception e) {
            result.setResult(1);
        }
        return result;
    }

    /**
     * 修改密码
     *
     * @return
     */
    @PostMapping("/removePassword")
    public String removePassword(String password, String newPassword, HttpSession httpSession) {
        try {

            String id = httpSession.getAttribute("id").toString();
            String type = httpSession.getAttribute("type").toString();

            if (type.equals("1")) {

                if (service.updatePassword(id, password, newPassword)) {
                    return "0";
                } else {
                    return "1";
                }
            } else {
                if (managerUserService.modifyPassword(id, password, newPassword)) {
                    return "0";
                } else {
                    return "1";
                }
            }
        } catch (Exception e) {
            return "1";
        }
    }

    /**
     * 获取用户信息
     *
     * @param httpSession
     * @return
     */
    @PostMapping("/message")
    public Result message(HttpSession httpSession) {

        Result result = new Result();
        try {

            String id = httpSession.getAttribute("id").toString();
            String type = httpSession.getAttribute("type").toString();

            if (type.equals("1")) {
                result.setData(service.query(id));
            } else {
                result.setData(managerUserService.gain(id));
            }
        } catch (Exception e) {
            result.setResult(1);
        }

        return result;
    }
}
