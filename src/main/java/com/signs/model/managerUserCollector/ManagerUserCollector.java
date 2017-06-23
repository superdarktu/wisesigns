package com.signs.model.managerUserCollector;

import javax.persistence.*;

@Table(name = "tb_manager_user_collector")
public class ManagerUserCollector {
    @Id
    private String id;

    @Column(name = "manager_user_id")
    private String managerUserId;

    @Column(name = "collector_id")
    private String collectorId;

    @Column(name = "collector_name")
    private String collectorName;

    @Column(name = "manager_user_type")
    private Integer managerUserType;

    @Column(name = "collector_code")
    private String collectorCode;

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
     * @return manager_user_id
     */
    public String getManagerUserId() {
        return managerUserId;
    }

    /**
     * @param managerUserId
     */
    public void setManagerUserId(String managerUserId) {
        this.managerUserId = managerUserId == null ? null : managerUserId.trim();
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
     * @return collector_name
     */
    public String getCollectorName() {
        return collectorName;
    }

    /**
     * @param collectorName
     */
    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName == null ? null : collectorName.trim();
    }

    /**
     * @return manager_user_type
     */
    public Integer getManagerUserType() {
        return managerUserType;
    }

    /**
     * @param managerUserType
     */
    public void setManagerUserType(Integer managerUserType) {
        this.managerUserType = managerUserType;
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
}