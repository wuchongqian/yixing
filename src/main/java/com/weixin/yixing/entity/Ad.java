package com.weixin.yixing.entity;

import java.util.Date;

public class Ad {
    private Integer id;

    private String openid;

    private String worksid;

    private String adUrl;

    private String adName;

    private String typeOfAd;

    private Byte deadline;

    private Integer numOfClicks;

    private Date createTime;

    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getWorksid() {
        return worksid;
    }

    public void setWorksid(String worksid) {
        this.worksid = worksid == null ? null : worksid.trim();
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl == null ? null : adUrl.trim();
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName == null ? null : adName.trim();
    }

    public String getTypeOfAd() {
        return typeOfAd;
    }

    public void setTypeOfAd(String typeOfAd) {
        this.typeOfAd = typeOfAd == null ? null : typeOfAd.trim();
    }

    public Byte getDeadline() {
        return deadline;
    }

    public void setDeadline(Byte deadline) {
        this.deadline = deadline;
    }

    public Integer getNumOfClicks() {
        return numOfClicks;
    }

    public void setNumOfClicks(Integer numOfClicks) {
        this.numOfClicks = numOfClicks;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}