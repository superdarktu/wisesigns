package com.signs.controller.mobile;

import com.signs.model.commons.Result;
import com.signs.service.manager.ManagerService;
import com.signs.service.managerUser.ManagerUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
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
    public Result saveUserImg(@RequestParam("file") MultipartFile file, HttpSession session) {
        Result result = new Result();
        try {
            String path;
            if (StringUtil.isEmpty(imagePathOn)||"1".equals(imagePathOn)){
                path = (this.getClass().getResource("/").toString() + "static/upload").replace("file:/", "");
            }else {
                path=imagePathOn;
            }
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
            String type = session.getAttribute("type").toString();
            String id = session.getAttribute("id").toString();
//            if ("1".equals(type)){
//                service.saveUserImg(fileName,id);
//            }else {
//                managerUserService.saveUserImg(fileName,id);
//            }
            result.setData(fileName);
            result.setMsg("添加成功");
        } catch (Exception ex) {
            result.setError(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }
        return result;
    }
}
