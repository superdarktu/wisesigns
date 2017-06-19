package com.signs.model.waterFountains;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_water_fountains")
public class WaterFountains {
    @Id
    private String id;

    private String code;

    private String place;

    @Column(name = "table_code")
    private String tableCode;

    private Integer type;

    @Column(name = "water_price")
    private Float waterPrice;

    @Column(name = "cost_scale")
    private Float costScale;

    private Float longitude;

    private Float latitude;

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
     * @return table_code
     */
    public String getTableCode() {
        return tableCode;
    }

    /**
     * @param tableCode
     */
    public void setTableCode(String tableCode) {
        this.tableCode = tableCode == null ? null : tableCode.trim();
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
     * @return longitude
     */
    public Float getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     */
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    /**
     * @return latitude
     */
    public Float getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     */
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
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