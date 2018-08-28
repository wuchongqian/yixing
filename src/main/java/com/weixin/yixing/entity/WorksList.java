package com.weixin.yixing.entity;

/**
 * @author wuchongqian1
 */
public class WorksList {
    private String worksName;

    private String worksId;

    private String authorName;

    private String authorId;

    private Integer numOfVotes;

    private String image;

    public String getWorksName() {
        return worksName;
    }

    public void setWorksName(String worksName) {
        this.worksName = worksName;
    }

    public String getWorksId() {
        return worksId;
    }

    public void setWorksId(String worksId) {
        this.worksId = worksId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Integer getNumOfVotes() {
        return numOfVotes;
    }

    public void setNumOfVotes(Integer numOfVotes) {
        this.numOfVotes = numOfVotes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
