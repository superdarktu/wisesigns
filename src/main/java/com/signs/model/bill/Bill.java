package com.signs.model.bill;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_bill")
public class Bill {
    @Id
    private String id;

    /**
     * 物业1  推广2
     */
    private Integer type;

    private String name;

    @Column(name = "income_day")
    private Float incomeDay;

    @Column(name = "income_month")
    private Float incomeMonth;

    private Float income;

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
     * 获取物业1  推广2
     *
     * @return type - 物业1  推广2
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置物业1  推广2
     *
     * @param type 物业1  推广2
     */
    public void setType(Integer type) {
        this.type = type;
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
     * @return income_day
     */
    public Float getIncomeDay() {
        return incomeDay;
    }

    /**
     * @param incomeDay
     */
    public void setIncomeDay(Float incomeDay) {
        this.incomeDay = incomeDay;
    }

    /**
     * @return income_month
     */
    public Float getIncomeMonth() {
        return incomeMonth;
    }

    /**
     * @param incomeMonth
     */
    public void setIncomeMonth(Float incomeMonth) {
        this.incomeMonth = incomeMonth;
    }

    /**
     * @return income
     */
    public Float getIncome() {
        return income;
    }

    /**
     * @param income
     */
    public void setIncome(Float income) {
        this.income = income;
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