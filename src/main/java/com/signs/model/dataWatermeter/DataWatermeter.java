package com.signs.model.dataWatermeter;

import javax.persistence.*;
import java.util.Date;

@Table(name = "data_watermeter")
public class DataWatermeter {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String code;

    private Integer type;

    private String batch;

    @Column(name = "get_time")
    private Date getTime;

    @Column(name = "import_temp")
    private Double importTemp;

    @Column(name = "export_temp")
    private Double exportTemp;

    @Column(name = "import_pressure")
    private Double importPressure;

    @Column(name = "export_pressure")
    private Double exportPressure;

    @Column(name = "moment_flow")
    private Double momentFlow;

    @Column(name = "total_flow")
    private Double totalFlow;

    private Integer wifi;

    @Column(name = "send_interval")
    private Integer sendInterval;

    @Column(name = "send_order")
    private String sendOrder;

    @Column(name = "signal_intensity")
    private Integer signalIntensity;

    private String band;

    @Column(name = "k_num")
    private String kNum;

    private Integer status;

    private Date time;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
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
     * @return type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return batch
     */
    public String getBatch() {
        return batch;
    }

    /**
     * @param batch
     */
    public void setBatch(String batch) {
        this.batch = batch == null ? null : batch.trim();
    }

    /**
     * @return get_time
     */
    public Date getGetTime() {
        return getTime;
    }

    /**
     * @param getTime
     */
    public void setGetTime(Date getTime) {
        this.getTime = getTime;
    }

    /**
     * @return import_temp
     */
    public Double getImportTemp() {
        return importTemp;
    }

    /**
     * @param importTemp
     */
    public void setImportTemp(Double importTemp) {
        this.importTemp = importTemp;
    }

    /**
     * @return export_temp
     */
    public Double getExportTemp() {
        return exportTemp;
    }

    /**
     * @param exportTemp
     */
    public void setExportTemp(Double exportTemp) {
        this.exportTemp = exportTemp;
    }

    /**
     * @return import_pressure
     */
    public Double getImportPressure() {
        return importPressure;
    }

    /**
     * @param importPressure
     */
    public void setImportPressure(Double importPressure) {
        this.importPressure = importPressure;
    }

    /**
     * @return export_pressure
     */
    public Double getExportPressure() {
        return exportPressure;
    }

    /**
     * @param exportPressure
     */
    public void setExportPressure(Double exportPressure) {
        this.exportPressure = exportPressure;
    }

    /**
     * @return moment_flow
     */
    public Double getMomentFlow() {
        return momentFlow;
    }

    /**
     * @param momentFlow
     */
    public void setMomentFlow(Double momentFlow) {
        this.momentFlow = momentFlow;
    }

    /**
     * @return total_flow
     */
    public Double getTotalFlow() {
        return totalFlow;
    }

    /**
     * @param totalFlow
     */
    public void setTotalFlow(Double totalFlow) {
        this.totalFlow = totalFlow;
    }

    /**
     * @return wifi
     */
    public Integer getWifi() {
        return wifi;
    }

    /**
     * @param wifi
     */
    public void setWifi(Integer wifi) {
        this.wifi = wifi;
    }

    /**
     * @return send_interval
     */
    public Integer getSendInterval() {
        return sendInterval;
    }

    /**
     * @param sendInterval
     */
    public void setSendInterval(Integer sendInterval) {
        this.sendInterval = sendInterval;
    }

    /**
     * @return send_order
     */
    public String getSendOrder() {
        return sendOrder;
    }

    /**
     * @param sendOrder
     */
    public void setSendOrder(String sendOrder) {
        this.sendOrder = sendOrder == null ? null : sendOrder.trim();
    }

    /**
     * @return signal_intensity
     */
    public Integer getSignalIntensity() {
        return signalIntensity;
    }

    /**
     * @param signalIntensity
     */
    public void setSignalIntensity(Integer signalIntensity) {
        this.signalIntensity = signalIntensity;
    }

    /**
     * @return band
     */
    public String getBand() {
        return band;
    }

    /**
     * @param band
     */
    public void setBand(String band) {
        this.band = band == null ? null : band.trim();
    }

    /**
     * @return k_num
     */
    public String getkNum() {
        return kNum;
    }

    /**
     * @param kNum
     */
    public void setkNum(String kNum) {
        this.kNum = kNum == null ? null : kNum.trim();
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
     * @return time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time
     */
    public void setTime(Date time) {
        this.time = time;
    }
}