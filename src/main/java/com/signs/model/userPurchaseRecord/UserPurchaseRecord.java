package com.signs.model.userPurchaseRecord;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_user_purchase_record")
public class UserPurchaseRecord {
    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    /**
     * 消费金额
     */
    private Float price;

    /**
     * 总消费金额
     */
    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "water_consumption")
    private Float waterConsumption;

    /**
     * 总水量
     */
    @Column(name = "total_consumption")
    private Float totalConsumption;

    /**
     * 单价
     */
    @Column(name = "unit_cost")
    private Float unitCost;

    /**
     * 余额
     */
    private Float balance;

    private String name;

    private String place;

    private Date ctime;

    @Column(name = "card_no")
    private String cardNo;

    @Column(name = "collector_id")
    private String collectorId;

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
     * 获取消费金额
     *
     * @return price - 消费金额
     */
    public Float getPrice() {
        return price;
    }

    /**
     * 设置消费金额
     *
     * @param price 消费金额
     */
    public void setPrice(Float price) {
        this.price = price;
    }

    /**
     * 获取总消费金额
     *
     * @return total_price - 总消费金额
     */
    public Float getTotalPrice() {
        return totalPrice;
    }

    /**
     * 设置总消费金额
     *
     * @param totalPrice 总消费金额
     */
    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return water_consumption
     */
    public Float getWaterConsumption() {
        return waterConsumption;
    }

    /**
     * @param waterConsumption
     */
    public void setWaterConsumption(Float waterConsumption) {
        this.waterConsumption = waterConsumption;
    }

    /**
     * 获取总水量
     *
     * @return total_consumption - 总水量
     */
    public Float getTotalConsumption() {
        return totalConsumption;
    }

    /**
     * 设置总水量
     *
     * @param totalConsumption 总水量
     */
    public void setTotalConsumption(Float totalConsumption) {
        this.totalConsumption = totalConsumption;
    }

    /**
     * 获取单价
     *
     * @return unit_cost - 单价
     */
    public Float getUnitCost() {
        return unitCost;
    }

    /**
     * 设置单价
     *
     * @param unitCost 单价
     */
    public void setUnitCost(Float unitCost) {
        this.unitCost = unitCost;
    }

    /**
     * 获取余额
     *
     * @return balance - 余额
     */
    public Float getBalance() {
        return balance;
    }

    /**
     * 设置余额
     *
     * @param balance 余额
     */
    public void setBalance(Float balance) {
        this.balance = balance;
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
     * @return place
     */
    public String getPlace() {
        return place;
    }

    /**
     * @param place
     */
    public void setPlace(String place) {
        this.place = place == null ? null : place.trim();
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
     * @return card_no
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * @param cardNo
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    /**
     * @return collector_id
     */
    public String getCollectorId() {
        return collectorId;
    }

    /**
     * @param collectorId
     */
    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId == null ? null : collectorId.trim();
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