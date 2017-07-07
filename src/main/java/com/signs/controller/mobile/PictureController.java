package com.signs.controller.mobile;

import com.signs.model.commons.Result;
import com.signs.model.manager.Manager;
import com.signs.model.managerUser.ManagerUser;
import com.signs.service.manager.ManagerService;
import com.signs.service.managerUser.ManagerUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;


@RestController
@RequestMapping("/app/picture")
public class PictureController {
    @Resource
    private ManagerService service;

    @Resource
    private ManagerUserService managerUserService;

    @Value("${upload.imagePathOn}")
    private String imagePathOn;

    @PostMapping("/uploadPicture")
    public Result saveUserImg(@RequestParam("file") MultipartFile file) {
        Result result = new Result();
        System.out.println("1");
        try {
            String path;
            if (StringUtil.isEmpty(imagePathOn)||"1".equals(imagePathOn)){
                path = (this.getClass().getResource("/").toString() + "static/upload").replace("file:/", "");
                System.out.println(path);
            }else {
                path=imagePathOn;
            }
            // String path = "C:\\ftp\\waterpro\\waterpro\\appimg\\";
            File dir = new File(path);
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdirs();
            }
            String name = file.getOriginalFilename();
            System.out.println(file.getName());
            if (!name.endsWith(".png") && !name.endsWith(".jpg") && !name.endsWith(".jpeg")) {
                result.setError("请上传图片文件");
                return result;
            }
            String fileName = (new Date()).getTime() + "" + (int) (Math.random() * 1000) + name.substring(name.lastIndexOf("."));
            Files.copy(file.getInputStream(), Paths.get(path + "/" + fileName));
            result.setData(fileName);
            result.setMsg("添加成功");
        } catch (Exception ex) {
            ex.printStackTrace();
            result.setError(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }
        return result;
    }
    @RequestMapping("/image")
    public Integer image(HttpServletResponse response, HttpSession session,String  img) {

        FileInputStream fis = null;
        OutputStream os = null;
        String path;
        if (StringUtil.isEmpty(imagePathOn)||"1".equals(imagePathOn)){
            path = (this.getClass().getResource("/").toString() + "static/upload").replace("file:/", "");
        }else {
            path=imagePathOn;
        }
        try {
            fis = new FileInputStream(path + "/" + img);
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
        return 0;
    }
}
