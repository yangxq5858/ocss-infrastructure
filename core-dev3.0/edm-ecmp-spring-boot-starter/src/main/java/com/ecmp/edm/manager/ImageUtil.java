package com.ecmp.edm.manager;

import com.ecmp.edm.entity.ImageRectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：使用JDK原生态类生成图片缩略图和裁剪图片
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-07-14 10:39      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class ImageUtil {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
    private static String DEFAULT_PREVFIX = "thumb_";
    private static Boolean DEFAULT_FORCE = false;

//    /**
//     * <p>Title: thumbnailImage</p>
//     * <p>Description: 根据图片路径生成缩略图 </p>
//     *
//     * @param imgFile 原图片路径
//     * @param w       缩略图宽
//     * @param h       缩略图高
//     * @param prevfix 生成缩略图的前缀
//     * @param force   是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
//     */
//    void thumbnailImage(File imgFile, int w, int h, String prevfix, boolean force) {
//        if (imgFile.exists()) {
//            try {
//                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
//                String types = Arrays.toString(ImageIO.getReaderFormatNames());
//                String suffix = null;
//                // 获取图片后缀
//                if (imgFile.getName().indexOf(".") > -1) {
//                    suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
//                }// 类型和图片后缀全部小写，然后判断后缀是否合法
//                if (suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0) {
//                    logger.error("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
//                    return;
//                }
//                logger.debug("target image's size, width:{}, height:{}.", w, h);
//                Image img = ImageIO.read(imgFile);
//                if (!force) {
//                    // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
//                    int width = img.getWidth(null);
//                    int height = img.getHeight(null);
//                    if ((width * 1.0) / w < (height * 1.0) / h) {
//                        if (width > w) {
//                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w / (width * 1.0)));
//                            logger.debug("change image's height, width:{}, height:{}.", w, h);
//                        }
//                    } else {
//                        if (height > h) {
//                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h / (height * 1.0)));
//                            logger.debug("change image's width, width:{}, height:{}.", w, h);
//                        }
//                    }
//                }
//                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
//                Graphics g = bi.getGraphics();
//                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
//                g.dispose();
//                String p = imgFile.getPath();
//                // 将图片保存在原目录并加上前缀
//                ImageIO.write(bi, suffix, new File(p.substring(0, p.lastIndexOf(File.separator)) + File.separator + prevfix + imgFile.getName()));
//            } catch (IOException e) {
//                logger.error("generate thumbnail image failed.", e);
//            }
//        } else {
//            logger.warn("the image is not exist.");
//        }
//    }
//
//    public void thumbnailImage(String imagePath, int w, int h, String prevfix, boolean force) {
//        File imgFile = new File(imagePath);
//        thumbnailImage(imgFile, w, h, prevfix, force);
//    }
//
//    public void thumbnailImage(String imagePath, int w, int h, boolean force) {
//        thumbnailImage(imagePath, w, h, DEFAULT_PREVFIX, force);
//    }
//
//    public void thumbnailImage(String imagePath, int w, int h) {
//        thumbnailImage(imagePath, w, h, DEFAULT_FORCE);
//    }

    /**
     * 根据原图与要求的缩略图比例，找到最合适的缩略图比例
     *
     * @param imageSize 原图尺寸
     * @param outSize   指定的输出尺寸
     * @return 调整后的尺寸
     */
    private static ImageRectangle changeImageSize(ImageRectangle imageSize, ImageRectangle outSize) {
        int width = imageSize.getWidth();
        int height = imageSize.getHeight();
        int w = outSize.getWidth();
        int h = outSize.getHeight();
        if ((width * 1.0) / w < (height * 1.0) / h) {
            if (width > w) {
                h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w / (width * 1.0)));
                logger.debug("change image's height, width:{}, height:{}.", w, h);
            }
        } else {
            if (height > h) {
                w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h / (height * 1.0)));
                logger.debug("change image's width, width:{}, height:{}.", w, h);
            }
        }
        return new ImageRectangle(w, h);
    }

    /**
     * 通过数据流生成缩略图的数据
     *
     * @param stream 数据流
     * @param suffix 文件后缀
     * @param size   缩略图尺寸
     * @return 生成缩略图的数据
     */
    public static byte[] thumbnailImage(InputStream stream, String suffix, ImageRectangle size) {
        if (Objects.isNull(stream)) {
            return null;
        }
        try {
            Image img = ImageIO.read(stream);
            // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
            int width = img.getWidth(null);
            int height = img.getHeight(null);
            ImageRectangle imageSize = new ImageRectangle(width, height);
            ImageRectangle changedSize = changeImageSize(imageSize, size);
            BufferedImage bi = new BufferedImage(changedSize.getWidth(), changedSize.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.getGraphics();
            g.drawImage(img, 0, 0, changedSize.getWidth(), changedSize.getHeight(), Color.LIGHT_GRAY, null);
            g.dispose();
            // 将图片保存为数据流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bi, suffix, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            logger.error("generate thumbnail image failed.", e);
        }
        return null;
    }
}
