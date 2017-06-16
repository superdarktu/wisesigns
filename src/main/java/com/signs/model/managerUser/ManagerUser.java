package com.signs.model.managerUser;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_manager_user")
public class ManagerUser {
    @Id
    private String id;

    private String account;

    private String password;

    private String name;

    @Column(name = "user_type")
    private Integer userType;

    private String phone;

    @Column(name = "cost_scale")
    private Float costScale;

    @Column(name = "ivision_proportion")
    private Float ivisionProportion;

    @Column(name = "water_price")
    private Float waterPrice;

    private Date ctime;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * @return account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account
     */
    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return user_type
     */
    public Integer getUserType() {
        return userType;
    }

    /**
     * @param userType
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * @return cost_scale
     */
    public Float getCostScale() {
        return costScale;
    }

    /**
     * @param costScale
     */
    public void setCostScale(Float costScale) {
        this.costScale = costScale;
    }

    /**
     * @return ivision_proportion
     */
    public Float getIvisionProportion() {
        return ivisionProportion;
    }

    /**
     * @param ivisionProportion
     */
    public void setIvisionProportion(Float ivisionProportion) {
        this.ivisionProportion = ivisionProportion;
    }

    /**
     * @return water_price
     */
    public Float getWaterPrice() {
        return waterPrice;
    }

    /**
     * @param waterPrice
     */
    public void setWaterPrice(Float waterPrice) {
        this.waterPrice = waterPrice;
    }

    /**
     * @return ctime
     */
    public Date getCtime() {
        return ctime;
    }

    /**
     * @param ctime
     */
    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }
}