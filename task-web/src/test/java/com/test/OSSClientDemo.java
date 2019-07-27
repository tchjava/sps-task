package com.test;

import com.gaby.util.OSSClientUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class OSSClientDemo {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("d:/pic1.png");
        FileInputStream is = new FileInputStream(file);
        //上传图片
        //OSSClientUtil.uploadFile("tjlimg", "img", file.getName(), is);

        //删除图片
        OSSClientUtil.deleteFile("tjlimg","goods","O3D1tvVHPU.png");

    }

}
