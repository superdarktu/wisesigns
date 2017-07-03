package com.signs.controller.manager;

import com.signs.model.commons.PageParam;
import com.signs.model.commons.Result;
import com.signs.model.manager.Manager;
import com.signs.model.managerUser.ManagerUser;
import com.signs.service.manager.ManagerService;
import com.signs.service.managerUser.ManagerUserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Resource
    private ManagerService service;

    @Resource
    private ManagerUserService managerUserService;

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
                        dto.setInfo("2");
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
            e.printStackTrace();
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
            if (!StringUtil.isEmpty(manager.getUserName()) && service.isHaveUsername(manager.getUserName())) {
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
            dto.setResult(0);
        } catch (Exception e) {
            e.printStackTrace();
            dto.setResult(1);
        }
        return dto;
    }

    @PostMapping("/pageUser")
    public Result pageUser(PageParam param, String keyWord) {
        Result dto = new Result();
        try {
            if (StringUtil.isEmpty(keyWord))
                keyWord = "";
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
            e.printStackTrace();
            dto.setData("1");
        }
        return dto;
    }

    /**
     * 修改密码
     *
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
            if (service.updatePassword(id, oldPassword, newPassword)) {
                dto.setData("0");
            }
        } catch (Exception e) {
            dto.setData("1");
        }
        return dto;
    }

    /**
     * 重置密码为123456
     *
     * @param id
     * @return
     */
    @PostMapping("/resetPassword")
    public Result resetPassword(String id) {
        Result dto = new Result();
        dto.setData("1");
        try {
            if (service.resetPassword(id)) {
                dto.setResult(0);
            }
        } catch (Exception e) {
            dto.setResult(1);
        }
        return dto;
    }

    @GetMapping("/uploadPicture")
    public Result saveUserImg(@RequestParam("file") MultipartFile file, HttpSession session) {
        Result result = new Result();
        try {
            String path = (this.getClass().getResource("/").toString() + "static/upload").replace("file:/", "");
            // String path = "C:\\ftp\\waterpro\\waterpro\\appimg\\";
            File dir = new File(path);
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdirs();
            }
            String name = file.getOriginalFilename();
            if (!name.endsWith(".png") && !name.endsWith(".jpg") && !name.endsWith(".jpeg")) {
                result.setError("请上传图片文件");
                return result;
            }
            String fileName = (new Date()).getTime() + "" + (int) (Math.random() * 1000) + name.substring(name.lastIndexOf("."));
            Files.copy(file.getInputStream(), Paths.get(path + "/" + fileName));
            String type = (String) session.getAttribute("type");
            String id = (String) session.getAttribute("id");
            type = "1";
            id = "027e7c39b427413eac745f7921ce33b8";
            if ("1".equals(type)) {
                //manager
                result.setData( service.saveUserImg(fileName, id));
            } else if ("2".equals(type)) {
                //managerUser
                result.setData(managerUserService.saveUserImg(fileName, id));

            }
            result.setMsg("添加成功");
        } catch (Exception ex) {
            result.setError(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }
        return result;
    }

    @RequestMapping("/image")
    public void image(HttpServletResponse response,HttpSession session){

        FileInputStream fis = null;
        OutputStream os = null;
        String type = (String) session.getAttribute("type");
        String img = "";
        String path = (this.getClass().getResource("/").toString() + "static/upload").replace("file:/", "");
        if ("1".equals(type)) {
            //manager
            Manager manager = service.query(session.getAttribute("id").toString());
            img = manager.getImg();
        } else if ("2".equals(type)) {
            //managerUser
            ManagerUser managerUser = managerUserService.gain(session.getAttribute("id").toString());
            img = managerUser.getImg();
        }
        try {
            fis = new FileInputStream(path+"/"+img);
            os = response.getOutputStream();
            int count = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((count = fis.read(buffer)) != -1) {
                os.write(buffer, 0, count);
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fis.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
