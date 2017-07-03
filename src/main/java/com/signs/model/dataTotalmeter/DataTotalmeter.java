package com.signs.model.dataTotalmeter;

import javax.persistence.*;
import java.util.Date;

@Table(name = "data_totalmeter")
public class DataTotalmeter {
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

    @Column(name = "total_heat")
    private Double totalHeat;

    @Column(name = "k_num")
    private String kNum;

    private Integer status;

    private Double cooding;

    @Column(name = "last_buy")
    private String lastBuy;

    @Column(name = "leave_buy")
    private String leaveBuy;

    @Column(name = "buy_order")
    private String buyOrder;

    @Column(name = "card_mode")
    private Integer cardMode;

    @Column(name = "have_card")
    private Integer haveCard;

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
     * @return total_heat
     */
    public Double getTotalHeat() {
        return totalHeat;
    }

    /**
     * @param totalHeat
     */
    public void setTotalHeat(Double totalHeat) {
        this.totalHeat = totalHeat;
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
     * @return cooding
     */
    public Double getCooding() {
        return cooding;
    }

    /**
     * @param cooding
     */
    public void setCooding(Double cooding) {
        this.cooding = cooding;
    }

    /**
     * @return last_buy
     */
    public String getLastBuy() {
        return lastBuy;
    }

    /**
     * @param lastBuy
     */
    public void setLastBuy(String lastBuy) {
        this.lastBuy = lastBuy == null ? null : lastBuy.trim();
    }

    /**
     * @return leave_buy
     */
    public String getLeaveBuy() {
        return leaveBuy;
    }

    /**
     * @param leaveBuy
     */
    public void setLeaveBuy(String leaveBuy) {
        this.leaveBuy = leaveBuy == null ? null : leaveBuy.trim();
    }

    /**
     * @return buy_order
     */
    public String getBuyOrder() {
        return buyOrder;
    }

    /**
     * @param buyOrder
     */
    public void setBuyOrder(String buyOrder) {
        this.buyOrder = buyOrder == null ? null : buyOrder.trim();
    }

    /**
     * @return card_mode
     */
    public Integer getCardMode() {
        return cardMode;
    }

    /**
     * @param cardMode
     */
    public void setCardMode(Integer cardMode) {
        this.cardMode = cardMode;
    }

    /**
     * @return have_card
     */
    public Integer getHaveCard() {
        return haveCard;
    }

    /**
     * @param haveCard
     */
    public void setHaveCard(Integer haveCard) {
        this.haveCard = haveCard;
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