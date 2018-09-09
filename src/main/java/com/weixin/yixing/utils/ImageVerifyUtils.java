package com.weixin.yixing.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by wuchongqian1 on 2017/6/23.
 */
public class ImageVerifyUtils {
    public static Boolean verifyImageName(String fileName){
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).trim().toLowerCase();

        String imageArry [] = {"bmp","dib","gif","jfif","jpe","jpeg","jpg","png", "tif", "tiff", "ico"};
        Boolean flag = false;
        for(int i = 0;i < imageArry.length; i++){
            if(imageArry[i].equals(suffix)){
                flag = true;
            }
        }
        return  flag;
    }
    public static Boolean verifyImageEntity(MultipartFile file){
        File tempFile = null;
        Boolean flag = true;
        try {
            tempFile=File.createTempFile("tmp", null);
            if(!tempFile.exists()){
                tempFile.mkdir();
            }
            String path = tempFile.getCanonicalPath();
            System.out.println(path);
            file.transferTo(tempFile);
            tempFile.deleteOnExit();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedImage src = ImageIO.read(tempFile);
            if(null == src || src.getWidth()<=0 || src.getHeight() <= 0) {
                flag = false;
                return flag;
            }

        } catch(IOException ex) {
            ex.printStackTrace();
        }
        return flag;
    }
}
