package com.signs.service.manager;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.signs.mapper.manager.ManagerMapper;
import com.signs.mapper.managerUser.ManagerUserMapper;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.manager.Manager;
import com.signs.util.auth.AccessToken;
import com.signs.util.auth.TokenManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.print.attribute.standard.MediaName;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ManagerService {

    @Resource
    private ManagerMapper mapper;


    @Resource
    private TokenManager tokenManager;

    @Resource
    private ManagerUserMapper managerUserMapper;

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    public Manager login(String username, String password) {
        HashMap<String, Object> parmHm = new HashMap<String, Object>();
        parmHm.put("userName", username);
        parmHm.put("password", password);
        Manager manager = mapper.login(parmHm);
        if (manager == null) return null;
        AccessToken accessToken = tokenManager.createToken(manager.getId(), manager.getStatus().toString());
        mapper.updateByPrimaryKeySelective(manager);
        return manager;
    }

    /**
     * 检查用户名是否存在
     *
     * @param username
     * @return
     */
    public boolean isHaveUsername(String username) {

        if (mapper.isHaveUsername(username) != null || managerUserMapper.selectCode(username) != null)
            return true;
        return false;
    }

    /**
     * 创建管理员
     *
     * @param manager
     * @return
     */
    public boolean createUser(Manager manager) {
        manager.setId(java.util.UUID.randomUUID().toString().replace("-", ""));
        manager.setCtime(new Date());
        manager.setStatus(0);
        manager.setType("manager");
        mapper.insert(manager);
        return true;
    }


    /**
     * 根据ids删除
     *
     * @param idstr
     * @return
     */
    @Transactional
    public boolean delete(String idstr) {

        String ids[] = idstr.split(",");
        for (String id : ids) {
            Manager manager = mapper.selectByPrimaryKey(id);
            if (manager == null) continue;
            manager.setStatus(1);
            mapper.updateByPrimaryKeySelective(manager);
        }
        return true;
    }


    /**
     * 修改用户信息
     *
     * @param manager
     * @return
     */
    public Manager save(Manager manager) {

        mapper.updateByPrimaryKeySelective(manager);

        return manager;
    }

    /**
     * 分页查询
     *
     * @param page
     * @param keyWord
     * @return
     */
    public PageInfo<Manager> page(PageParam page, String keyWord) {

        if (page.getPageNo() != null && page.getPageSize() != null) {
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        }
        List<Manager> result = mapper.findByAdmin(keyWord);
        return new PageInfo(result);
    }

    /**
     * 重置密码为123456
     *
     * @param id
     * @return
     */
    public boolean resetPassword(String id) {
        Manager temp = mapper.selectByPrimaryKey(id);
        temp.setPassword("123456");
        mapper.updateByPrimaryKeySelective(temp);

        return true;
    }

    /**
     * 修改自身账号密码
     *
     * @param id
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public boolean updatePassword(String id, String oldPassword, String newPassword) {
        boolean b = false;
        Manager temp = mapper.selectByPrimaryKey(id);
        if (temp.getPassword().equals(oldPassword)) {
            temp.setPassword(newPassword);
            mapper.updateByPrimaryKeySelective(temp);
            b = true;
        }

        return b;
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public Manager query(String id){

        return mapper.selectByPrimaryKey(id);
    }


}
