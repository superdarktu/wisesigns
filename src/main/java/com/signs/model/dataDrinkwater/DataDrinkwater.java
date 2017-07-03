package com.signs.model.dataDrinkwater;

import javax.persistence.*;
import java.util.Date;

@Table(name = "data_drinkwater")
public class DataDrinkwater {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String code;

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

    private Integer status;

    @Column(name = "tap_status")
    private Integer tapStatus;

    private Date time;

    private Date getTime;


    public Date getGetTime() {
        return getTime;
    }

    public void setGetTime(Date getTime) {
        this.getTime = getTime;
    }

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