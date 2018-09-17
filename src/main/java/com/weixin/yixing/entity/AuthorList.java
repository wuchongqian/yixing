package com.weixin.yixing.entity;

import java.util.Date;
import java.util.List;

public class AuthorList {
    private String authorUuid;

    private String authorName;

    private String phone;

    private Integer like;

    private String introductionOfAuthor;

    private String sumOfVotes;

    private String weChatOpenId;

    private List worksList;

    public String getAuthorUuid() {
        return authorUuid;
    }

    public void setAuthorUuid(String authorUuid) {
        this.authorUuid = authorUuid;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public String getIntroductionOfAuthor() {
        return introductionOfAuthor;
    }

    public void setIntroductionOfAuthor(String introductionOfAuthor) {
        this.introductionOfAuthor = introductionOfAuthor;
    }

    public String getSumOfVotes() {
        return sumOfVotes;
    }

    public void setSumOfVotes(String sumOfVotes) {
        this.sumOfVotes = sumOfVotes;
    }

    public String getWeChatOpenId() {
        return weChatOpenId;
    }

    public void setWeChatOpenId(String weChatOpenId) {
        this.weChatOpenId = weChatOpenId;
    }

    public List getWorksList() {
        return worksList;
    }

    public void setWorksList(List worksList) {
        this.worksList = worksList;
    }
}
