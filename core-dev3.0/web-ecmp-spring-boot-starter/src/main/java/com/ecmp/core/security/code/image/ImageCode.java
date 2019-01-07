package com.ecmp.core.security.code.image;

import com.ecmp.core.security.code.ValidateCode;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 图形验证码
 */
public class ImageCode extends ValidateCode {

    /**
     * 图形
     */
    private String image;

    public ImageCode() {
    }

    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code, expireIn);

        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "JPEG", byteArrayStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this.image = new sun.misc.BASE64Encoder().encodeBuffer(byteArrayStream.toByteArray());
        this.image = new Base64().encodeToString(byteArrayStream.toByteArray());
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
