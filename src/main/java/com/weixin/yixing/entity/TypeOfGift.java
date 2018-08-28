package com.weixin.yixing.entity;

public class TypeOfGift {
    private Integer id;

    private String typeName;

    private Integer valueOfGift;

    private Integer voteOfGift;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public Integer getValueOfGift() {
        return valueOfGift;
    }

    public void setValueOfGift(Integer valueOfGift) {
        this.valueOfGift = valueOfGift;
    }

    public Integer getVoteOfGift() {
        return voteOfGift;
    }

    public void setVoteOfGift(Integer voteOfGift) {
        this.voteOfGift = voteOfGift;
    }
}