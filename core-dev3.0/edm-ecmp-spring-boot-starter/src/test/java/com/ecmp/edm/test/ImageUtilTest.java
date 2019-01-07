package com.ecmp.edm.test;

import com.ecmp.edm.entity.ImageRectangle;
import com.ecmp.edm.manager.ImageUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-07-14 10:50      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class ImageUtilTest {

//    @Test
//    public void thumbnailImage(){
//        String fileName = "D:\\TempWork\\Downloads\\1.jpg";
//        new ImageUtil().thumbnailImage(fileName,100,150);
//    }

    @Test
    public void thumbnailImageByte() throws Exception{
        String fileName = "D:\\TempWork\\Downloads\\1.jpg";
        String outFileName = "D:\\TempWork\\Downloads\\thumb_1.jpg";
        InputStream stream = new FileInputStream(new File(fileName));
        byte[] thumbData = ImageUtil.thumbnailImage(stream,"jpg",new ImageRectangle(150,100));
        Assert.assertNotNull(thumbData);
        FileOutputStream file = new FileOutputStream(outFileName);
        file.write(thumbData);
        file.close();
    }
}
