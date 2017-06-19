package com.signs.model.watermeter;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_watermeter")
public class Watermeter {
    private String id;

    private String code;

    private Integer status;

    @Column(name = "total_code")
    private String totalCode;

    @Column(name = "collector_code")
    private String collectorCode;

    @Column(name = "flow_day")
    private Float flowDay;

    @Column(name = "flow_month")
    private Float flowMonth;

    @Column(name = "flow_total")
    private Float flowTotal;

    @Column(name = "tap_status")
    private Integer tapStatus;

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
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return total_code
     */
    public String getTotalCode() {
        return totalCode;
    }

    /**
     * @param totalCode
     */
    public void setTotalCode(String totalCode) {
        this.totalCode = totalCode == null ? null : totalCode.trim();
    }

    /**
     * @return collector_code
     */
    public String getCollectorCode() {
        return collectorCode;
    }

    /**
     * @param collectorCode
     */
    public void setCollectorCode(String collectorCode) {
        this.collectorCode = collectorCode == null ? null : collectorCode.trim();
    }

    /**
     * @return flow_day
     */
    public Float getFlowDay() {
        return flowDay;
    }

    /**
     * @param flowDay
     */
    public void setFlowDay(Float flowDay) {
        this.flowDay = flowDay;
    }

    /**
     * @return flow_month
     */
    public Float getFlowMonth() {
        return flowMonth;
    }

    /**
     * @param flowMonth
     */
    public void setFlowMonth(Float flowMonth) {
        this.flowMonth = flowMonth;
    }

    /**
     * @return flow_total
     */
    public Float getFlowTotal() {
        return flowTotal;
    }

    /**
     * @param flowTotal
     */
    public void setFlowTotal(Float flowTotal) {
        this.flowTotal = flowTotal;
    }

    /**
     * @return tap_status
     */
    public Integer getTapStatus() {
        return tapStatus;
    }

    /**
     * @param tapStatus
     */
    public void setTapStatus(Integer tapStatus) {
        this.tapStatus = tapStatus;
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