package com.weixin.yixing.entity;

import java.io.Serializable;
import java.util.Date;

public class File implements Serializable {
    private Integer id;

    private String fileUuid;

    private String fileOriginalName;

    private String fileOssKey;

    private String fileOssBucket;

    private Long groupId;

    private Date creationTime;

    private Date modifyTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid == null ? null : fileUuid.trim();
    }

    public String getFileOriginalName() {
        return fileOriginalName;
    }

    public void setFileOriginalName(String fileOriginalName) {
        this.fileOriginalName = fileOriginalName == null ? null : fileOriginalName.trim();
    }

    public String getFileOssKey() {
        return fileOssKey;
    }

    public void setFileOssKey(String fileOssKey) {
        this.fileOssKey = fileOssKey == null ? null : fileOssKey.trim();
    }

    public String getFileOssBucket() {
        return fileOssBucket;
    }

    public void setFileOssBucket(String fileOssBucket) {
        this.fileOssBucket = fileOssBucket == null ? null : fileOssBucket.trim();
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fileUuid=").append(fileUuid);
        sb.append(", fileOriginalName=").append(fileOriginalName);
        sb.append(", fileOssKey=").append(fileOssKey);
        sb.append(", fileOssBucket=").append(fileOssBucket);
        sb.append(", groupId=").append(groupId);
        sb.append(", creationTime=").append(creationTime);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
