package com.signs.model.aiarmHistory;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_aiarm_history")
public class AiarmHistory {
    @Id
    private String id;

    @Column(name = "water_fountains_code")
    private String waterFountainsCode;

    private String place;

    private String reason;

    @Column(name = "property_name")
    private String propertyName;

    @Column(name = "property_id")
    private String propertyId;

    private Float longitude;

    private Float latitude;

    private Date ctime;

    @Column(name = "fix_time")
    private Date fixTime;

    private Float duration;

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
     * @return water_fountains_code
     */
    public String getWaterFountainsCode() {
        return waterFountainsCode;
    }

    /**
     * @param waterFountainsCode
     */
    public void setWaterFountainsCode(String waterFountainsCode) {
        this.waterFountainsCode = waterFountainsCode == null ? null : waterFountainsCode.trim();
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
     * @return reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    /**
     * @return property_name
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @param propertyName
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName == null ? null : propertyName.trim();
    }

    /**
     * @return property_id
     */
    public String getPropertyId() {
        return propertyId;
    }

    /**
     * @param propertyId
     */
    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId == null ? null : propertyId.trim();
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

    /**
     * @return fix_time
     */
    public Date getFixTime() {
        return fixTime;
    }

    /**
     * @param fixTime
     */
    public void setFixTime(Date fixTime) {
        this.fixTime = fixTime;
    }

    /**
     * @return duration
     */
    public Float getDuration() {
        return duration;
    }

    /**
     * @param duration
     */
    public void setDuration(Float duration) {
        this.duration = duration;
    }
}