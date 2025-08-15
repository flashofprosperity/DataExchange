package org.example.dataSource.domain;


import java.util.Date;

public class CornorModelNo {

    private int id;

    private String modelNo;

    private String modelName;

    private String capactity;

    private String eccDistance;

    private Date updateTime;

    private String remark;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getCapactity() {
        return capactity;
    }

    public void setCapactity(String capactity) {
        this.capactity = capactity;
    }

    public String getEccDistance() {
        return eccDistance;
    }

    public void setEccDistance(String ecc_distance) {
        this.eccDistance = ecc_distance;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }




}
