package com.weixin.yixing.entity;

import java.util.Date;

public class WorksInfo {
    private Integer id;

    private String worksNum;

    private String worksUuid;

    private String worksName;

    private String authorId;

    private Integer numberOfVotes;

    private String introductionOfWorks;

    private String image;

    private Integer numOfClicks;

    private Integer category;

    private String activityId;

    private String status;

    private String deleteTag;

    private Date createTime;

    private Date modifyTime;

    private String memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorksNum() {
        return worksNum;
    }

    public void setWorksNum(String worksNum) {
        this.worksNum = worksNum == null ? null : worksNum.trim();
    }

    public String getWorksUuid() {
        return worksUuid;
    }

    public void setWorksUuid(String worksUuid) {
        this.worksUuid = worksUuid == null ? null : worksUuid.trim();
    }

    public String getWorksName() {
        return worksName;
    }

    public void setWorksName(String worksName) {
        this.worksName = worksName == null ? null : worksName.trim();
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId == null ? null : authorId.trim();
    }

    public Integer getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(Integer numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public String getIntroductionOfWorks() {
        return introductionOfWorks;
    }

    public void setIntroductionOfWorks(String introductionOfWorks) {
        this.introductionOfWorks = introductionOfWorks == null ? null : introductionOfWorks.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Integer getNumOfClicks() {
        return numOfClicks;
    }

    public void setNumOfClicks(Integer numOfClicks) {
        this.numOfClicks = numOfClicks;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId == null ? null : activityId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getDeleteTag() {
        return deleteTag;
    }

    public void setDeleteTag(String deleteTag) {
        this.deleteTag = deleteTag == null ? null : deleteTag.trim();
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}