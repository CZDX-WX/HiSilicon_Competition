package com.czdxwx.comptition.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * 
 * @TableName device
 */
@TableName(value ="device")
public class Device implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备是否在线
     */
    private Integer isOnline;

    /**
     * 持有人
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
     * 设备名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设备名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 设备是否在线
     */
    public Integer getIsOnline() {
        return isOnline;
    }

    /**
     * 设备是否在线
     */
    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    /**
     * 持有人
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 持有人
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
        Device other = (Device) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getIsOnline() == null ? other.getIsOnline() == null : this.getIsOnline().equals(other.getIsOnline()))
            && (this.getOwner() == null ? other.getOwner() == null : this.getOwner().equals(other.getOwner()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getIsOnline() == null) ? 0 : getIsOnline().hashCode());
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
        sb.append(", name=").append(name);
        sb.append(", isOnline=").append(isOnline);
        sb.append(", owner=").append(owner);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}