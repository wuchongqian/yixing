package com.weixin.yixing.entity.vo;

import javax.validation.constraints.NotEmpty;;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 
 * @Author chengtinghua
 * @Date 2017/4/27 23:45
 */
public class GetWidthResizedImageUrlRequest implements Serializable{

    @NotEmpty(message = "文件UUID不能为空")
    private String fileUuid;

    //当为0时，输出原图
    @NotNull(message = "图片宽度不能为空")
    @Range(min = 0, max = 4096, message = "图片宽度最小不能低于0，最大不能超过4096")
    private Integer imageWidth;

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }
}
