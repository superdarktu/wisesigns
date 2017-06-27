package com.signs.model.userRechargeRecord;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_user_recharge_record")
public class UserRechargeRecord {
    @Id
    private String id;

    private String name;

    @Column(name = "user_id")
    private String userId;

    /**
     * 充值金额
     */
    private Float price;

    /**
     * 累计充值金额
     */
    @Column(name = "total_price")
    private Float totalPrice;

    private Float balance;

    private Date ctime;

    @Column(name = "order_id")
    private String orderId;

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
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取充值金额
     *
     * @return price - 充值金额
     */
    public Float getPrice() {
        return price;
    }

    /**
     * 设置充值金额
     *
     * @param price 充值金额
     */
    public void setPrice(Float price) {
        this.price = price;
    }

    /**
     * 获取累计充值金额
     *
     * @return total_price - 累计充值金额
     */
    public Float getTotalPrice() {
        return totalPrice;
    }

    /**
     * 设置累计充值金额
     *
     * @param totalPrice 累计充值金额
     */
    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return balance
     */
    public Float getBalance() {
        return balance;
    }

    /**
     * @param balance
     */
    public void setBalance(Float balance) {
        this.balance = balance;
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

    /**
     * @return order_id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }
}