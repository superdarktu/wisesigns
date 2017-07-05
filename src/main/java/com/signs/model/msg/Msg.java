package com.signs.model.msg;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_msg")
public class Msg {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String phone;

    private String capital;

    @Column(name = "end_time")
    private Date endTime;

    private Date ctime;

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
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * @return capital
     */
    public String getCapital() {
        return capital;
    }

    /**
     * @param capital
     */
    public void setCapital(String capital) {
        this.capital = capital == null ? null : capital.trim();
    }

    /**
     * @return end_time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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