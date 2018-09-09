package com.weixin.yixing.entity.vo;


import javax.validation.constraints.NotEmpty;

public class UploadFileByStringBase64Request {

    //文件名需带有后缀
    @NotEmpty(message = "原始文件名称不能为空")
    private String originalFileName;

    private Long groupId;

    //文件流转为base64字符串
    @NotEmpty(message = "文件内容不能为空")
    private String fileContent;

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}

