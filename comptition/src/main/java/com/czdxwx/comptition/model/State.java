package com.czdxwx.comptition.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @TableName state
 */
@TableName(value ="state")
public class State implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 
     */
    private String province;

    /**
     * 
     */
    private String city;

    /**
     * 
     */
    private String district;

    /**
     * 
     */
    private String temperature;

    /**
     * 日期时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dateTime;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 拥有者
     */
    private String owner;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 纬度
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 纬度
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * 经度
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 经度
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     */
    public String getProvince() {
        return province;
    }

    /**
     * 
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 
     */
    public String getCity() {
        return city;
    }

    /**
     * 
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 
     */
    public String getDistrict() {
        return district;
    }

    /**
     * 
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * 
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * 
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    /**
     * 日期时间
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * 日期时间
     */
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * 设备名称
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * 设备名称
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * 拥有者
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 拥有者
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        State other = (State) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLatitude() == null ? other.getLatitude() == null : this.getLatitude().equals(other.getLatitude()))
            && (this.getLongitude() == null ? other.getLongitude() == null : this.getLongitude().equals(other.getLongitude()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getDistrict() == null ? other.getDistrict() == null : this.getDistrict().equals(other.getDistrict()))
            && (this.getTemperature() == null ? other.getTemperature() == null : this.getTemperature().equals(other.getTemperature()))
            && (this.getDateTime() == null ? other.getDateTime() == null : this.getDateTime().equals(other.getDateTime()))
            && (this.getDeviceName() == null ? other.getDeviceName() == null : this.getDeviceName().equals(other.getDeviceName()))
            && (this.getOwner() == null ? other.getOwner() == null : this.getOwner().equals(other.getOwner()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLatitude() == null) ? 0 : getLatitude().hashCode());
        result = prime * result + ((getLongitude() == null) ? 0 : getLongitude().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getDistrict() == null) ? 0 : getDistrict().hashCode());
        result = prime * result + ((getTemperature() == null) ? 0 : getTemperature().hashCode());
        result = prime * result + ((getDateTime() == null) ? 0 : getDateTime().hashCode());
        result = prime * result + ((getDeviceName() == null) ? 0 : getDeviceName().hashCode());
        result = prime * result + ((getOwner() == null) ? 0 : getOwner().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", district=").append(district);
        sb.append(", temperature=").append(temperature);
        sb.append(", dateTime=").append(dateTime);
        sb.append(", deviceName=").append(deviceName);
        sb.append(", owner=").append(owner);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}